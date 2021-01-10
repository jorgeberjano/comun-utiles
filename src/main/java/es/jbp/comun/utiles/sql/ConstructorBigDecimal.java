package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Constructor de un objeto BigDecimal a partir del resltado de una consulta.
 * @author jberjano
 */
public class ConstructorBigDecimal extends ConstructorEntidad<BigDecimal> {
    
    private String campo;

    public ConstructorBigDecimal() {
    }
    
    public ConstructorBigDecimal(String campo) {
        this.campo = campo;
    }
        
    @Override
    protected BigDecimal construirEntidad(AdaptadorResultSet rs) throws SQLException {        
        if (campo != null) {
            return rs.getBigDecimal(campo);
        } else {
            CampoBd campoBd = rs.getMetadatos().getCampo(1);
            return rs.getBigDecimal(campoBd.getNombre());
        }
    }
    
}
