package es.jbp.comun.utiles.sql.sincronizacion;

import java.util.List;
import es.jbp.comun.utiles.sql.AdaptadorResultSet;
import es.jbp.comun.utiles.sql.ConstructorEntidad;
import es.jbp.comun.utiles.sql.esquema.CampoBd;
import es.jbp.comun.utiles.sql.esquema.TablaBd;

/**
 *
 * @author jberjano
 */
public class ConstructorEntidadGenerica extends ConstructorEntidad<EntidadGenerica> {

    private TablaBd tabla;

    public ConstructorEntidadGenerica(TablaBd tabla) {
        this.tabla = tabla;
    }

    @Override
    protected EntidadGenerica construirEntidad(AdaptadorResultSet rs) throws Exception {
        EntidadGenerica entidad = new EntidadGenerica();
        List<CampoBd> campos = tabla.getCampos();
        for (CampoBd campo : campos) {
            Object valor = rs.get(campo);
            if (campo.isClavePrimaria()) {
                entidad.setPk(campo.getNombre(), valor);
            } else {
                entidad.set(campo.getNombre(), valor);
            }
        }
        return entidad;
    }

}
