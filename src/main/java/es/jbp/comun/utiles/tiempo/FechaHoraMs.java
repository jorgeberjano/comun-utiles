package es.jbp.comun.utiles.tiempo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase que representa una fecha, una hora.y un milisegundo
 *
 * @author jberjano
 */
@JsonDeserialize(using = DeserializadorFechaHoraMs.class)
@JsonSerialize(using = SerializadorFechaHoraMs.class)
public class FechaHoraMs extends FechaAbstracta {

    private final static FechaHoraMs presente;
    private static DateFormat formatoPorDefecto;

    static {
        formatoPorDefecto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        presente = new FechaHoraMs();
    }

    public static DateFormat getFormatoPorDefecto() {
        return FechaHoraMs.formatoPorDefecto;
    }

    public static void setFormatoPorDefecto(DateFormat formatoPorDefecto) {
        FechaHoraMs.formatoPorDefecto = formatoPorDefecto;
    }

    public static FechaHoraMs presente() {
        return presente;
    }

    public static FechaHoraMs ahora() {
        return new FechaHoraMs();
    }

    public static FechaHoraMs parsear(String texto) {
        return texto != null ? new FechaHoraMs(texto) : null;
    }

    public static FechaHoraMs parsear(String texto, String formato) {
        Calendar calendar = crearCalendar(texto, new SimpleDateFormat(formato));
        return calendar == null ? null : new FechaHoraMs(calendar);
    }

    public FechaHoraMs() {
        super(Calendar.getInstance(), formatoPorDefecto);
    }

    public FechaHoraMs(FechaAbstracta fecha) {
        super(fecha.calendar, formatoPorDefecto);
    }

    public FechaHoraMs(Fecha fecha, Hora hora) {
        super(fecha, hora, formatoPorDefecto);
    }

    public FechaHoraMs(Calendar calendar) {
        super(calendar, formatoPorDefecto);
    }

    public FechaHoraMs(Date date) {
        super(date, formatoPorDefecto);
    }

    public FechaHoraMs(long milisegundos) {
        super(milisegundos, formatoPorDefecto);
    }

    public FechaHoraMs(int dia, int mes, int anno, int hora, int min, int seg, int ms) {
        super(dia, mes, anno, hora, min, seg, ms, formatoPorDefecto);
    }

    public FechaHoraMs(String texto) {
        super(texto, formatoPorDefecto);
    }

    public int getMilisegundo() {
        return calendar.get(Calendar.MILLISECOND);
    }

    @Override
    public boolean esPresente() {
        return this == presente;
    }
}
