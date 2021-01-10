package es.jbp.comun.utiles.sql.sincronizacion;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import es.jbp.comun.utiles.sql.ConstructorLong;
import es.jbp.comun.utiles.sql.EjecutorScript;
import es.jbp.comun.utiles.sql.EjecutorSentenciaDelete;
import es.jbp.comun.utiles.sql.EjecutorSentenciaInsert;
import es.jbp.comun.utiles.sql.EjecutorSentenciaSelect;
import es.jbp.comun.utiles.sql.EjecutorSentenciaUpdate;
import es.jbp.comun.utiles.sql.GestorConexiones;
import es.jbp.comun.utiles.sql.MetadatosBd;
import es.jbp.comun.utiles.sql.esquema.CampoBd;
import es.jbp.comun.utiles.sql.esquema.EsquemaBd;
import es.jbp.comun.utiles.sql.esquema.ExportadorDDL;
import es.jbp.comun.utiles.sql.esquema.TablaBd;

/**
 * Sincroniza la tablas de dos bases de datos.
 *
 * @author jberjano
 */
public class SincronizadorBd {

    protected final GestorConexiones gestorConexionOrigen;
    protected final GestorConexiones gestorConexionDestino;

    public SincronizadorBd(GestorConexiones gestorConexionOrigen,
            GestorConexiones gestorConexionDestino) {
        this.gestorConexionOrigen = gestorConexionOrigen;
        this.gestorConexionDestino = gestorConexionDestino;
    }

    public boolean sincronizarTablas(EsquemaBd esquema, boolean soloInsertar) throws Exception {

        List<TablaBd> tablas = esquema.getTablas();
        for (TablaBd tabla : tablas) {
            boolean ok;
            if (soloInsertar) {
                ok = copiarTabla(tabla);
            } else {
                ok = sincronizarTabla(tabla);
            }
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    protected String getSelectTabla(TablaBd tabla) {
        return "SELECT * FROM " + tabla.getNombre();
    }

    protected String buscarClavePrimaria(TablaBd tabla) {
        String clavePrimaria = null;
        for (CampoBd campo : tabla.getCampos()) {
            if (campo.isClavePrimaria()) {
                clavePrimaria = campo.getNombre();
                break;
            }
        }
        if (clavePrimaria == null) {
            clavePrimaria = "ID";
        }
        return clavePrimaria;
    }

    protected String getSelectClavesPrimarias(TablaBd tabla) {
        String clavePrimaria = buscarClavePrimaria(tabla);
        return "SELECT " + clavePrimaria + " FROM " + tabla.getNombre();
    }
    
    private boolean copiarTabla(TablaBd tabla) throws Exception {

        EjecutorSentenciaSelect ejecutorSelectOrigen = new EjecutorSentenciaSelect(gestorConexionOrigen);

        String sql = getSelectTabla(tabla);
        ConstructorEntidadGenerica constructor = new ConstructorEntidadGenerica(tabla);
        List<EntidadGenerica> listaEntidades = ejecutorSelectOrigen.obtenerListaEntidades(sql, constructor);

        for (EntidadGenerica entidad : listaEntidades) {
            boolean ok = insertarFila(tabla, entidad);
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    private boolean sincronizarTabla(TablaBd tabla) throws Exception {

        EjecutorSentenciaSelect ejecutorSelectOrigen = new EjecutorSentenciaSelect(gestorConexionOrigen);
        EjecutorSentenciaSelect ejecutorSelectDestino = new EjecutorSentenciaSelect(gestorConexionDestino);

        ConstructorLong constructorLong = new ConstructorLong();
        
        String sqlClavesPrimarias = getSelectClavesPrimarias(tabla);

        List<Long> listaClavesPrimariasOrigen = ejecutorSelectOrigen.obtenerListaEntidades(sqlClavesPrimarias, constructorLong);
        Set<Long> clavesPrimariasOrigen = new HashSet<Long>(listaClavesPrimariasOrigen);

        List<Long> listaClavesPrimariasDestino = ejecutorSelectDestino.obtenerListaEntidades(sqlClavesPrimarias, constructorLong);
        Set<Long> clavesPrimariasDestino = new HashSet<Long>(listaClavesPrimariasDestino);

        String sql = getSelectTabla(tabla);
        ConstructorEntidadGenerica constructor = new ConstructorEntidadGenerica(tabla);
        List<EntidadGenerica> listaEntidadesOrigen = ejecutorSelectOrigen.obtenerListaEntidades(sql, constructor);

        for (EntidadGenerica entidad : listaEntidadesOrigen) {
            boolean ok;
            boolean existeEntidad = clavesPrimariasDestino.contains(entidad.getPk());
            if (existeEntidad) {
                ok = modificarFila(tabla, entidad);
            } else {
                ok = insertarFila(tabla, entidad);
            }
            if (!ok) {
                return false;
            }
        }

        for (Long id : listaClavesPrimariasDestino) {
            if (!clavesPrimariasOrigen.contains(id)) {
                boolean ok = borrarFila(tabla, id);
                if (!ok) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean insertarFila(TablaBd tabla, EntidadGenerica entidad) throws Exception {

        EjecutorSentenciaInsert insert = new EjecutorSentenciaInsert(gestorConexionDestino);
        insert.setTabla(tabla.getNombre());

        List<CampoBd> campos = tabla.getCampos();
        for (CampoBd campo : campos) {
            insert.agregarCampo(campo.getNombre(), entidad.get(campo.getNombre()));
        }

        return insert.ejecutar();
    }

    private boolean modificarFila(TablaBd tabla, EntidadGenerica entidad) throws Exception {
        EjecutorSentenciaUpdate update = new EjecutorSentenciaUpdate(gestorConexionDestino);
        update.setTabla(tabla.getNombre());

        List<CampoBd> campos = tabla.getCampos();
        for (CampoBd campo : campos) {
            Object valor = entidad.get(campo.getNombre());
            if (campo.isClavePrimaria()) {
                update.agregarPk(campo.getNombre(), valor);
            } else {
                update.agregarCampo(campo.getNombre(), valor);
            }
        }

        return update.ejecutar();
    }

    private boolean borrarFila(TablaBd tabla, Long id) throws Exception {
        EjecutorSentenciaDelete delete = new EjecutorSentenciaDelete(gestorConexionDestino);
        delete.setTabla(tabla.getNombre());
        String clavePrimaria = buscarClavePrimaria(tabla);
        delete.agregarPk(clavePrimaria, id);
        return delete.ejecutar();
    }

    public boolean crearBaseDatosDestino(List<String> listaTablas) throws Exception {
        MetadatosBd metadatos = new MetadatosBd(
                gestorConexionOrigen.obtenerConexion(), listaTablas);
        EsquemaBd esquemaBd = metadatos.getEsquemaBd();

        EjecutorScript ejecutor = new EjecutorScript(gestorConexionDestino);
        ExportadorDDL exportadorDDL = new ExportadorDDL(gestorConexionDestino.getFormateadorSql());

        try {
            String sqlBorrado = exportadorDDL.getSqlBorrado(esquemaBd);
            ejecutor.ejecutar(sqlBorrado);
        } catch (SQLException ex) {
            System.out.println("Borrado no realizado");
        }

        String sqlCreacion = exportadorDDL.getSqlCreacion(esquemaBd);
        return ejecutor.ejecutar(sqlCreacion);

    }
}
