package es.jbp.comun.utiles.sql;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jorge
 */
class ConstructorGenerico extends ConstructorEntidad<Map> {

    @Override
    protected Map construirEntidad(AdaptadorResultSet rs) throws Exception {
        MetadatosConsulta metadatos = rs.getMetadatos();
        Map entidad = new HashMap();
        for (Campo campo : metadatos.getListaCampos()) {
            Object valor = rs.get(campo.getNombre());
            entidad.put(campo.getNombre(), valor);
        }
        return entidad;
    }
    
}
