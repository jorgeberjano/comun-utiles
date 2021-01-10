package es.jbp.comun.utiles.sql.compatibilidad;

import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.sql.TipoDato;
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
public class FormateadorSQLite extends FormateadorBasico {

    public static String formatoFecha = "yyyy-MM-dd";
    public static String formatoFechaHora = "yyyy-MM-dd HH:mm:ss";
    public static String formatoFechaHoraMs = "yyyy-MM-dd HH:mm:ss.SSS";

    public FormateadorSQLite() {
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
        return "date('" + fecha.formatear(formatoFecha) + "')";
    }

    @Override
    public String getFechaHoraSql(FechaHora fechaHora) {
        if (fechaHora == null) {
            return "null";
        }
        return "datetime('" + fechaHora.formatear(formatoFechaHora) + "')";
    }

    @Override
    public String getFechaHoraMsSql(FechaHoraMs fechaHora) {
        if (fechaHora == null) {
            return "null";
        }
        return "datetime('" + fechaHora.formatear(formatoFechaHoraMs) + "')";
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
        return "select datetime('now', 'localtime')";
    }

    @Override
    public String getFucionNVL(String valor, String valorSiEsNulo) {
        return "IFNULL(" + valor + ", " + valorSiEsNulo + ")";
    }

    @Override
    public String getFechaActual() {
        return "date('now', 'localtime')";
    }

    @Override
    public String getFechaHoraActual() {
        return "datetime('now', 'localtime')";
    }

    @Override
    public String getFechaHoraMsActual() {
        return "datetime('now', 'localtime')";
    }

    @Override
    public Set<Integer> getCodigosErrorDesconexion() {
        Set<Integer> codigos = new HashSet<Integer>();
//        codigos.add(17002);
//        codigos.add(17008);
        return codigos;
    }

    @Override
    public Object convertirAlPersistir(Object valor) {
        if (valor instanceof Boolean) {
            return (Boolean) valor ? 1 : 0;
        } else if (valor instanceof Fecha) {
            return ((Fecha) valor).formatear("yyyy-MM-dd");
        } else if (valor instanceof FechaHora) {
            return ((FechaHora) valor).formatear("yyyy-MM-dd HH:mm:ss");
        } else if (valor instanceof FechaHoraMs) {
            return ((FechaHoraMs) valor).formatear("yyyy-MM-dd HH:mm:ss.SSS");
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
    
    public FechaHoraMs toFechaHoraMs(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof Integer || valor instanceof Long) {
            return new FechaHoraMs(Conversion.toLong(valor));
        }
        String valorTexto = Conversion.toString(valor);
        FechaHoraMs fechaHoraMs = FechaHoraMs.parsear(valorTexto, formatoFechaHoraMs);
        if (fechaHoraMs != null) {
            return fechaHoraMs;
        }
        fechaHoraMs = FechaHoraMs.parsear(valorTexto, formatoFechaHora);        
        if (fechaHoraMs != null) {
            return fechaHoraMs;
        }
        fechaHoraMs = FechaHoraMs.parsear(valorTexto, formatoFecha);               
        return fechaHoraMs;
    }

    public FechaHora toFechaHora(Object valor) {
        FechaHoraMs fechaHoraMs = toFechaHoraMs(valor);
        return fechaHoraMs == null ? null : fechaHoraMs.getFechaHora();
    }
    
    public Fecha toFecha(Object valor) {
        FechaHoraMs fechaHoraMs = toFechaHoraMs(valor);
        return fechaHoraMs == null ? null : fechaHoraMs.getFecha();
    }
}
