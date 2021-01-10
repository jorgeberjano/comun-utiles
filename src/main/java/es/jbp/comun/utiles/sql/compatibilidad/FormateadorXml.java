package es.jbp.comun.utiles.sql.compatibilidad;

import java.util.Set;
import es.jbp.comun.utiles.sql.esquema.CampoBd;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaHora;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;

/**
 * Formateador para bases de datos XML
 * @author jberjano
 */
public class FormateadorXml extends FormateadorBasico {

    private static final String formatoFecha = "yyyy-MM-dd";

    @Override
    public String getFechaSql(Fecha fecha) {
        if (fecha == null) {
            return "null";
        }
        return "'" + fecha.formatear(formatoFecha) + "'";
    }

    @Override
    public String getFechaHoraSql(FechaHora fechaHora) {
        if (fechaHora == null) {
            return "null";
        }
        return "'" + fechaHora.formatear(formatoFecha + " hh:mm:ss") + "'";
    }


    @Override
    public String getFechaHoraMsSql(FechaHoraMs fechaHora) {
        if (fechaHora == null) {
            return "null";
        }
        return "'" + fechaHora.formatear(formatoFecha + " hh:mm:ss.SSS") + "'";
    }

    @Override
    public String getBooleanSql(Boolean valor) {
        return valor ? "1" : "0";
    }

    @Override
    public String getNombreTipoSql(CampoBd campo) {
        switch (campo.getTipoDato()) {
            case CADENA:
                return "VARCHAR2(" + campo.getTamano() + ")";
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
        return "select SYSDATE from DUAL";
    }

    @Override
    public String getFucionNVL(String valor, String valorSiEsNulo) {
        return "NVL(" + valor + ", " + valorSiEsNulo + ")";
    }

    @Override
    public String getFechaActual() {
        return "SYSDATE";
    }

    @Override
    public String getFechaHoraActual() {
        return "SYSDATE";
    }

    @Override
    public String getFechaHoraMsActual() {
        return "SYSDATE";
    }

    @Override
    public Set<Integer> getCodigosErrorDesconexion() {
        return null;
    }

    @Override
    public Object convertirAlPersistir(Object valor) {
        if (valor instanceof Boolean) {
            return (Boolean) valor ? 1 : 0;
        }
        return valor;
    }
}
