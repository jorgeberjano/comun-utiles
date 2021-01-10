package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import java.sql.SQLException;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;

/**
 * Constructor de un objeto FechaHoraMs a partir del resultado de una consulta.
 * @author jberjano
 */
public class ConstructorFechaHoraMs extends ConstructorEntidad<FechaHoraMs> {

    private String campo;

    public ConstructorFechaHoraMs() {
    }

    public ConstructorFechaHoraMs(String campo) {
        this.campo = campo;
    }

    @Override
    protected FechaHoraMs construirEntidad(AdaptadorResultSet rs) throws Exception {
        if (campo != null) {
            return rs.getFechaHoraMs(campo);
        } else {
            CampoBd campoBd = rs.getMetadatos().getCampo(1);
            return rs.getFechaHoraMs(campoBd.getNombre());
        }
    }
}
