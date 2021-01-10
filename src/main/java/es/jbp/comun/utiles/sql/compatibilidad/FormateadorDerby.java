package es.jbp.comun.utiles.sql.compatibilidad;

import java.util.Set;
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
 * Formateador para bases de datos Derby
 * @author jberjano
 */
public class FormateadorDerby extends FormateadorBasico {

    public FormateadorDerby() {
        mapaSimbolos.put("TRUE", "TRUE");
        mapaSimbolos.put("FALSE", "FALSE");
        mapaSimbolos.put("AS", "AS");
    }

    @Override
    public String getBooleanSql(Boolean valor) {
        return valor ? "1" : "0";
    }

    @Override
    public String getFechaSql(Fecha fecha) {
        return "DATE('" + fecha.formatear("yyyy-MM-dd") + "')";
    }

    @Override
    public String getFechaHoraSql(FechaHora fechaHora) {
        return "TIMESTAMP('" + fechaHora.formatear("yyyy-MM-dd HH:mm:ss") + "')";
    }

    @Override
    public String getFechaHoraMsSql(FechaHoraMs fechaHora) {
        return "TIMESTAMP('" + fechaHora.formatear("yyyy-MM-dd HH:mm:ss.SSS") + "')";
    }

    @Override
    public String getNombreTipoSql(CampoBd campo) {
        switch (campo.getTipoDato()) {
            case CADENA:
                return "VARCHAR(" + campo.getTamano() + ")";
            case ENTERO:
                return "BIGINT";
            case REAL:
                return "DOUBLE";
            case BOOLEANO:
                return "SMALLINT";
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
        return "select CURRENT_TIMESTAMP from SYSIBM.SYSDUMMY1";
    }

    @Override
    public String getFucionNVL(String valor, String valorSiEsNulo) {
        return "case when " + valor + " is null then " + valorSiEsNulo + " else " + valor + " end";
    }

    @Override
    public String getFechaActual() {
        return "CURRENT_TIMESTAMP";
    }

    @Override
    public String getFechaHoraActual() {
        return "CURRENT_TIMESTAMP";
    }

    @Override
    public String getFechaHoraMsActual() {
        return "CURRENT_TIMESTAMP";
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
