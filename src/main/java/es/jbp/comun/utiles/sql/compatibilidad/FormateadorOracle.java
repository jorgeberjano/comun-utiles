package es.jbp.comun.utiles.sql.compatibilidad;

import java.util.HashSet;
import java.util.Set;
import es.jbp.comun.utiles.sql.esquema.CampoBd;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaHora;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;

/**
 * Formateador para bases de datos Oracle
 * @author jberjano
 */
public class FormateadorOracle extends FormateadorBasico {

    public static String formatoFecha = "dd/MM/yyyy";

    public FormateadorOracle() {
        mapaSimbolos.put("TRUE", "1");
        mapaSimbolos.put("FALSE", "0");
        mapaSimbolos.put("AS", "");
    }

    @Override
    public String getBooleanSql(Boolean valor) {
        return valor ? "1" : "0";
    }

    @Override
    public String getFechaSql(Fecha fecha) {
        if (fecha == null) {
            return "null";
        }
        return "TO_DATE('" + fecha.formatear(formatoFecha) + "', '" + formatoFecha + "')";
    }

    @Override
    public String getFechaHoraSql(FechaHora fechaHora) {
        if (fechaHora == null) {
            return "null";
        }
        return "TO_DATE('" + fechaHora.formatear(formatoFecha + " HH:mm:ss") + "', '" + formatoFecha + " hh24:mi:ss')";
    }

    @Override
    public String getFechaHoraMsSql(FechaHoraMs fechaHora) {
        if (fechaHora == null) {
            return "null";
        }
        return "TO_TIMESTAMP('" + fechaHora.formatear(formatoFecha + " HH:mm:ss.SSS") + "', '" + formatoFecha + " hh24:mi:ss.FF3')";
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
        return "TRUNC(SYSDATE)";
    }

    @Override
    public String getFechaHoraActual() {
        return "SYSDATE";
    }

    @Override
    public String getFechaHoraMsActual() {
        return "CURRENT_TIMESTAMP";
    }

    @Override
    public Set<Integer> getCodigosErrorDesconexion() {
        Set<Integer> codigos = new HashSet<Integer>();
        codigos.add(17002);
        codigos.add(17008);
        return codigos;
    }

    @Override
    public Object convertirAlPersistir(Object valor) {
        if (valor instanceof Boolean) {
            return (Boolean) valor ? 1 : 0;
        }
        return valor;
    }

    @Override
    public String getComparacionNoCase(String operador1, String operando, String operador2) {
        return new StringBuilder()
                .append("lower(")
                .append(operador1)
                .append(") ")
                .append(operando)
                .append(" lower(")
                .append(operador2)
                .append(")")
                .toString();
    }
}
