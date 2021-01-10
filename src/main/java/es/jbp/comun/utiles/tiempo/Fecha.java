package es.jbp.comun.utiles.tiempo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase que representa una fecha (sin hora).
 *
 * @author jberjano
 */
@JsonDeserialize(using = DeserializadorFecha.class)
@JsonSerialize(using = SerializadorFecha.class)
public class Fecha extends FechaAbstracta {

    private static DateFormat formatoPorDefecto = new SimpleDateFormat("dd/MM/yyyy");
    private final static Fecha presente;

    static {
        formatoPorDefecto = new SimpleDateFormat("dd/MM/yyyy");
        presente = new Fecha();
    }

    public static DateFormat getFormatoPorDefecto() {
        return formatoPorDefecto;
    }

    public static void setFormatoPorDefecto(DateFormat formatoPorDefecto) {
        Fecha.formatoPorDefecto = formatoPorDefecto;
    }

    public static Fecha getPresente() {
        return presente;
    }

    public static Fecha parsearFecha(String texto) {
        return texto != null ? new Fecha(texto) : null;
    }

    public Fecha() {
        super(Calendar.getInstance(), formatoPorDefecto);
        normalizar();
    }

    public Fecha(String texto) {
        super(texto, formatoPorDefecto);
        normalizar();
    }

    public Fecha(String texto, String formato) {
        super(texto, new SimpleDateFormat(formato));
        normalizar();
    }

    public Fecha(int dia, int mes, int anno) {
        super(formatoPorDefecto);
        calendar.set(Calendar.YEAR, anno);
        calendar.set(Calendar.MONTH, mes - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        normalizar();
    }

    public Fecha(Calendar calendar) {
        super(calendar, formatoPorDefecto);
        normalizar();
    }

    public Fecha(Date date) {
        super(date, formatoPorDefecto);
        normalizar();
    }

    public Fecha(Timestamp timestamp) {
        super(timestamp, formatoPorDefecto);
        normalizar();
    }

    public Fecha(FechaAbstracta fecha) {
        super(fecha.calendar, formatoPorDefecto);
        normalizar();
    }
    
     public Fecha(long milisegundos) {
        super(milisegundos, formatoPorDefecto);
        normalizar();
    }

    public static Fecha hoy() {
        return new Fecha();
    }
    
    public static Fecha parsear(String texto) {
        return texto != null ? new Fecha(texto) : null;
    }

    public static Fecha parsear(String texto, String formato) {
        return new Fecha(crearCalendar(texto, new SimpleDateFormat(formato)));
    }

    private void normalizar() {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
    }

    @Override
    public boolean esPresente() {
        return this == presente;
    }

//    protected static Calendar crearCalendar(String texto) {
//        return crearCalendar(texto, formatoPorDefecto);
//    }
    public Fecha masDias(int dias) {
        if (esNula()) {
            return this;
        }
        Calendar cal = getCalendar();
        cal.add(Calendar.DAY_OF_YEAR, dias);
        return new Fecha(cal);
    }

    public Fecha masMeses(int meses) {
        if (esNula()) {
            return this;
        }
        Calendar cal = getCalendar();
        cal.add(Calendar.MONTH, meses);
        return new Fecha(cal);
    }
}
