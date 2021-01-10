package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.sql.SQLException;

/**
 * Constructor de un objeto Long a partir del resltado de una consulta.
 * @author jberjano
 */
public class ConstructorLong extends ConstructorEntidad<Long> {
    
    private String campo;

    public ConstructorLong() {
    }
    
    public ConstructorLong(String campo) {
        this.campo = campo;
    }
        
    @Override
    protected Long construirEntidad(AdaptadorResultSet rs) throws SQLException {        
        if (campo != null) {
            return rs.getLong(campo);
        } else {
            CampoBd campoBd = rs.getMetadatos().getCampo(1);
            return rs.getLong(campoBd.getNombre());
        }
    }
    
}
