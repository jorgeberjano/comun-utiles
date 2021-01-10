package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.sql.SQLException;

/**
 * Constructor de un array de bytes a partir del resultado de una consulta.
 * @author jberjano
 */
public class ConstructorBytes extends ConstructorEntidad<byte[]> {
        private String campo;

    public ConstructorBytes() {
    }
    
    public ConstructorBytes(String campo) {
        this.campo = campo;
    }
        
    @Override
    protected byte[] construirEntidad(AdaptadorResultSet rs) throws Exception {        
        if (campo != null) {
            return rs.getBytes(campo);
        } else {
            CampoBd campoBd = rs.getMetadatos().getCampo(1);
            return rs.getBytes(campoBd.getNombre());
        }
    }
}
