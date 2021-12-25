package es.jbp.comun.utiles.sql.compatibilidad;

import es.jbp.comun.utiles.sql.esquema.CampoBd;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaHora;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;

/**
 * Formateador para bases de datos Access
 * @author jberjano
 */
public class FormateadorMsSql extends FormateadorBasico {

    public FormateadorMsSql() {
        mapaSimbolos.put("TRUE", "1");
        mapaSimbolos.put("FALSE", "0");
        mapaSimbolos.put("AS", "AS");
    }

    @Override
    public String getBooleanSql(Boolean valor) {
        return valor ? "1" : "0";
    }

    @Override
    public String getFechaSql(Fecha fecha) {
        return "#" + fecha.formatear("MM/dd/yyyy") + "#";
    }

    @Override
    public String getFechaHoraSql(FechaHora fechaHora) {
        return "#" + fechaHora.formatear("MM/dd/yyyy HH:mm:ss") + "#";
    }

    @Override
    public String getFechaHoraMsSql(FechaHoraMs fechaHora) {
        return "#" + fechaHora.formatear("MM/dd/yyyy HH:mm:ss.SSS") + "#";
    }


    @Override
    public String getNombreTipoSql(CampoBd campo) {
        switch (campo.getTipoDato()) {
            case CADENA:
                return "Text(" + campo.getTamano() + ")";
            case ENTERO:
                return "Long";
            case REAL:
                return "Double";
            case BOOLEANO:
                return "Yes/No";
            case FECHA:
                return "Date";
            case FECHA_HORA:
                return "Date/Time";
            case BYTES:
                return "Memo";
            default:
                return "";
        }
    }

    @Override
    public String getSelectConsultaFechaHora() {
        return "select now()";
    }

    @Override
    public String getFucionNVL(String valor, String valorSiEsNulo) {
        return "Nz(" + valor + ", " + valorSiEsNulo + ")";
    }

    @Override
    public String getFechaActual() {
        return "now()";
    }

    @Override
    public String getFechaHoraActual() {
        return "now()";
    }

    @Override
    public String getFechaHoraMsActual() {
        return "now()";
    }

    @Override
    public String getCaracterComodin() {
        return "%";
    }

    @Override
    public String getNombreParametro(int indice) {
        return "@P" + indice;
    }
}
