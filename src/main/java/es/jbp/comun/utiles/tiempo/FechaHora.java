package es.jbp.comun.utiles.tiempo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase que representa una fecha y una hora.
 *
 * @author jberjano
 */
@JsonDeserialize(using = DeserializadorFechaHora.class)
@JsonSerialize(using = SerializadorFechaHora.class)
public class FechaHora extends FechaAbstracta {

    private final static FechaHora presente;
    private static DateTimeFormatter formatoPorDefecto;

    static {
        formatoPorDefecto = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        presente = new FechaHora();
    }

    public static DateTimeFormatter getFormatoPorDefecto() {
        return FechaHora.formatoPorDefecto;
    }

    public static void setFormatoPorDefecto(DateTimeFormatter formatoPorDefecto) {
        FechaHora.formatoPorDefecto = formatoPorDefecto;
    }

    public static FechaHora presente() {
        return presente;
    }

    public static FechaHora ahora() {
        return new FechaHora();
    }

    public static FechaHora parsear(String texto) {
        return texto != null ? new FechaHora(texto) : null;
    }

    public static FechaHora parsear(String texto, String formato) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
        return new FechaHora(crearLocalDateTime(texto, formateador), formateador);
    }

    public FechaHora() {
        super(Calendar.getInstance(), formatoPorDefecto);
        normalizar();
    }

    public FechaHora(LocalDateTime localDateTime) {
        super(localDateTime, formatoPorDefecto);
        normalizar();
    }

    public FechaHora(LocalDateTime localDateTime, DateTimeFormatter formato) {
        super(localDateTime, formato);
        normalizar();
    }

    public FechaHora(FechaAbstracta fecha) {
        super(fecha.localDateTime, formatoPorDefecto);
        normalizar();
    }

    public FechaHora(Fecha fecha, Hora hora) {
        super(fecha, hora, formatoPorDefecto);
        normalizar();
    }

    public FechaHora(Calendar calendar) {
        super(calendar, formatoPorDefecto);
        normalizar();
    }

    public FechaHora(Date date) {
        super(date, formatoPorDefecto);
        normalizar();
    }

    public FechaHora(long milisegundos) {
        super(milisegundos, formatoPorDefecto);
        normalizar();
    }

    public FechaHora(int dia, int mes, int anno, int hora, int min, int seg) {
        super(dia, mes, anno, hora, min, seg, 0, formatoPorDefecto);
        normalizar();
    }

    public FechaHora(String texto) {
        super(texto, formatoPorDefecto);
        normalizar();
    }

    public FechaHora(String texto, String formato) {
        super(texto, DateTimeFormatter.ofPattern(formato));
        normalizar();
    }

    private void normalizar() {
        if (this.localDateTime != null) {
            this.localDateTime = localDateTime.withNano(0);
        }
    }

    @Override
    public boolean esPresente() {
        return this == presente;
    }

    public FechaHora masHoras(int horas) {
        if (esNula()) {
            return this;
        }
        LocalDateTime result = localDateTime.plusHours(horas);
        return new FechaHora(result, formato);
    }

    public FechaHora masMinutos(int minutos) {
        if (esNula()) {
            return this;
        }
        LocalDateTime result = localDateTime.plusMinutes(minutos);
        return new FechaHora(result, formato);
    }

    public FechaHora masSegundos(int segundos) {
        if (esNula()) {
            return this;
        }
        LocalDateTime result = localDateTime.plusSeconds(segundos);
        return new FechaHora(result, formato);
    }
}
