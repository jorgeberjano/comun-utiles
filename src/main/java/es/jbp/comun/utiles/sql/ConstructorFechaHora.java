package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.sql.SQLException;
import es.jbp.comun.utiles.tiempo.FechaHora;

/**
 * Constructor de un objeto FechaHora a partir del resultado de una consulta.
 * @author jberjano
 */
public class ConstructorFechaHora extends ConstructorEntidad<FechaHora> {

    private String campo;

    public ConstructorFechaHora() {
    }

    public ConstructorFechaHora(String campo) {
        this.campo = campo;
    }

    @Override
    protected FechaHora construirEntidad(AdaptadorResultSet rs) throws Exception {
        if (campo != null) {
            return rs.getFechaHora(campo);
        } else {
            CampoBd campoBd = rs.getMetadatos().getCampo(1);
            return rs.getFechaHora(campoBd.getNombre());
        }
    }
}
