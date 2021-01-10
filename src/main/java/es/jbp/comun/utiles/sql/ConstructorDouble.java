package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.sql.SQLException;

/**
 * Constructor de un objeto Double a partir del resultado de una consulta.
 * @author jberjano
 */
public class ConstructorDouble extends ConstructorEntidad<Double> {
    
    private String campo;

    public ConstructorDouble() {
    }
    
    public ConstructorDouble(String campo) {
        this.campo = campo;
    }
        
    @Override
    protected Double construirEntidad(AdaptadorResultSet rs) throws Exception {        
        if (campo != null) {
            return rs.getDouble(campo);
        } else {
            CampoBd campoBd = rs.getMetadatos().getCampo(1);
            return rs.getDouble(campoBd.getNombre());
        }
    }
    
}
