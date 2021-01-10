package es.jbp.comun.utiles.sql.compatibilidad;

import java.util.Set;
import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.sql.esquema.CampoBd;
import static es.jbp.comun.utiles.sql.TipoDato.BOOLEANO;
import static es.jbp.comun.utiles.sql.TipoDato.BYTES;
import static es.jbp.comun.utiles.sql.TipoDato.CADENA;
import static es.jbp.comun.utiles.sql.TipoDato.ENTERO;
import static es.jbp.comun.utiles.sql.TipoDato.FECHA;
import static es.jbp.comun.utiles.sql.TipoDato.FECHA_HORA;
import static es.jbp.comun.utiles.sql.TipoDato.REAL;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaHora;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;

/**
 * Formateador para bases de datos H2
 * @author jberjano
 */
public class FormateadorH2 extends FormateadorBasico {

    @Override
    public String getFechaSql(Fecha fecha) {
        return "parsedatetime('" + fecha.formatear("dd-MM-yyyy") + "', 'dd-MM-yyyy')";
    }

    @Override
    public String getFechaHoraSql(FechaHora fechaHora) {
        return "parsedatetime('" + fechaHora.formatear("dd-MM-yyyy hh:mm:ss") + "', 'dd-MM-yyyy hh:mm:ss')";
    }

    @Override
    public String getFechaHoraMsSql(FechaHoraMs fechaHora) {
        return "parsedatetime('" + fechaHora.formatear("dd-MM-yyyy hh:mm:ss.SSS") + "', 'dd-MM-yyyy hh:mm:ss.SSS')";
    }

    @Override
    public String getBooleanSql(Boolean valor) {
        return valor ? "1" : "0";
    }

    @Override
    public String getNombreTipoSql(CampoBd campo) {
        switch (campo.getTipoDato()) {
            case CADENA:
                return "VARCHAR(" + campo.getTamano() + ")";
            case ENTERO:
                return "NUMBER(" + campo.getTamano() + ")";
            case REAL:
                return "DECIMAL(" + campo.getTamano() + ", " + campo.getDecimales() + ")";
            case BOOLEANO:
                return "NUMBER(1)";
            case FECHA:
                return "DATE";
            case FECHA_HORA:
                return "TIMESTAMP";
            case BYTES:
                return "BLOB";
            default:
                return "";
        }
    }

    @Override
    public String getSelectConsultaFechaHora() {
        return "select CURRENT_TIMESTAMP() from DUAL";
    }

    @Override
    public String getFucionNVL(String valor, String valorSiEsNulo) {
        return "NVL(" + valor + ", " + valorSiEsNulo + ")";
    }

    @Override
    public String getFechaActual() {
        return "CURRENT_TIMESTAMP()";
    }

    @Override
    public String getFechaHoraActual() {
        return "CURRENT_TIMESTAMP()";
    }

    @Override
    public String getFechaHoraMsActual() {
        return "CURRENT_TIMESTAMP()";
    }

    @Override
    public Set<Integer> getCodigosErrorDesconexion() {
        return null;
    }

    @Override
    public Object convertirAlPersistir(Object valor) {
        return valor;
    }
}
