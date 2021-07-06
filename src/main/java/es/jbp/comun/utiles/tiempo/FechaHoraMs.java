package es.jbp.comun.utiles.tiempo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static DateTimeFormatter formatoPorDefecto;

    static {
        formatoPorDefecto = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");
        presente = new FechaHoraMs();
    }

    public static DateTimeFormatter getFormatoPorDefecto() {
        return FechaHoraMs.formatoPorDefecto;
    }

    public static void setFormatoPorDefecto(DateTimeFormatter formatoPorDefecto) {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        LocalDateTime localDateTime = crearLocalDateTime(texto, formatter);
        return localDateTime == null ? null : new FechaHoraMs(localDateTime, formatter);
    }

    public FechaHoraMs() {
        super(Calendar.getInstance(), formatoPorDefecto);
    }

    public FechaHoraMs(LocalDateTime localDateTime) {
        super(localDateTime, formatoPorDefecto);
    }

    public FechaHoraMs(LocalDateTime localDateTime, DateTimeFormatter formato) {
        super(localDateTime, formato);
    }

    public FechaHoraMs(FechaAbstracta fecha) {
        super(fecha.localDateTime, formatoPorDefecto);
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

    @Override
    public boolean esPresente() {
        return this == presente;
    }
}
