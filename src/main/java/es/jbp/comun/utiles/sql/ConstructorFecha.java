package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.sql.SQLException;
import es.jbp.comun.utiles.tiempo.Fecha;

/**
 * Constructor de un objeto Fecha a partir del resltado de una consulta.
 * @author jberjano
 */
public class ConstructorFecha extends ConstructorEntidad<Fecha> {

    private String campo;

    public ConstructorFecha() {
    }

    public ConstructorFecha(String campo) {
        this.campo = campo;
    }

    @Override
    protected Fecha construirEntidad(AdaptadorResultSet rs) throws Exception {
        if (campo != null) {
            return rs.getFecha(campo);
        } else {
            CampoBd campoBd = rs.getMetadatos().getCampo(1);
            return rs.getFecha(campoBd.getNombre());
        }
    }
}
