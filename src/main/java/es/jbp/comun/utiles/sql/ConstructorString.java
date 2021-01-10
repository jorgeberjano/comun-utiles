package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.sql.SQLException;

/**
 * Constructor de un objeto String a partir del resltado de una consulta.
 * @author jberjano
 */
public class ConstructorString extends ConstructorEntidad<String> {
    
    private String campo;

    public ConstructorString() {
    }
    
    public ConstructorString(String campo) {
        this.campo = campo;
    }
        
    @Override
    protected String construirEntidad(AdaptadorResultSet rs) throws SQLException {             
        if (campo != null) {
            return rs.getString(campo);
        } else {
            CampoBd campoBd = rs.getMetadatos().getCampo(1);
            return rs.getString(campoBd.getNombre());
        }
    }
    
}
