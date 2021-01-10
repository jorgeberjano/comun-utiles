package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jorge
 */
public class DaoGenerico extends Dao {
    private final ConstructorGenerico constructor = new ConstructorGenerico();    
    private final Map<String, List<CampoBd>> mapaListaCampos = new HashMap<>();
    private final FormateadorSql formateadorSql;

    public DaoGenerico(GestorConexiones gestorConexiones) {
        super(gestorConexiones);
        constructor.setFormateadorSql(gestorConexiones.getFormateadorSql());
        formateadorSql = gestorConexiones.getFormateadorSql();
    }
    
    public List<Map> getEntidades(String sql) {

        limpiarMensajeError();
        List entidades = new ArrayList<Map>();
        try {            
            entidades = crearEjecutorSentenciaSelect().obtenerListaEntidades(sql, constructor);
        } catch (Exception ex) {
            reportarExcepcion("Error al obtener los eventos de un portador en un sensor desde una fecha", ex);
        }
        return entidades;
    }

    public boolean borrarDatosTabla(String nombreTabla) {
        boolean ok = false;
        EjecutorSentenciaDelete ejecutor = crearEjecutorSentenciaDelete();        
        ejecutor.setTabla(nombreTabla);
        
        try {
            ok = ejecutor.ejecutar();
        } catch (Exception ex) {
            reportarExcepcion("Error al borrar los datos de la tabla " + nombreTabla, ex);
        }
        return ok;
    }

    public boolean insertarDatosTabla(String nombreTabla, List entidades) {       
        
        limpiarMensajeError();
        for (Object entidad : entidades) {
            if (entidad instanceof Map) {
                boolean ok = insertarEntidad(nombreTabla, (Map) entidad);
                if (!ok) {
                    return false;
                }
            }
        }        
        return true;
    }
        
    public boolean insertarEntidad(String nombreTabla, Map entidad) {
        boolean ok = false;
        
        List<CampoBd> campos = obtenerCamposTabla(nombreTabla);
        if (campos == null) {
            return false;
        }
        
        EjecutorSentenciaGuardado ejecutor = crearEjecutorSentenciaInsert();     
        try {
            ejecutor.setTabla(nombreTabla);
            for (CampoBd campo : campos) {
                String nombreCampo = campo.getNombre();
                Object valor = entidad.get(nombreCampo);
                TipoDato tipo = campo.getTipoDato();
                valor = formateadorSql.convertirValor(valor, tipo);
                valor = formateadorSql.convertirAlPersistir(valor);                
                ejecutor.agregarCampo(nombreCampo, valor);
            }
            ok = ejecutor.ejecutar();
            //entidad.setIdSec(secuencia.getValorGenerado());
        } catch (Exception ex) {
            reportarExcepcion("Error al insertar una fila de la tabla " + nombreTabla, ex);
        }
        return ok;
    }

    public List<CampoBd> obtenerCamposTabla(String tabla) {
        List<CampoBd> listaCampos = mapaListaCampos.get(tabla);
        if (listaCampos != null) {
            return listaCampos;
        }
        
        Connection conexion = null;       
        try {
            conexion = gestorConexiones.obtenerConexion();
            MetadatosBd metadatos = new MetadatosBd(conexion);
            listaCampos = metadatos.getCampos(null, tabla);
            mapaListaCampos.put(tabla, listaCampos);
            return listaCampos;
        } catch (Exception ex) {
            reportarExcepcion("Error al obtener los metadatos de la tabla " + tabla, ex);
            return null;
        } finally {
            if (conexion != null) {
                gestorConexiones.liberarConexion(conexion);
            }
        }                
    }
}
