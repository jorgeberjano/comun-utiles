package es.jbp.comun.utiles.tiempo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
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

    private static DateTimeFormatter formatoPorDefecto;
    private final static Fecha presente;

    static {
        formatoPorDefecto =new DateTimeFormatterBuilder()
                .appendPattern("dd/MM/yyyy[ HH:mm:ss]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        presente = new Fecha();
    }

    public static DateTimeFormatter getFormatoPorDefecto() {
        return formatoPorDefecto;
    }

    public static void setFormatoPorDefecto(DateTimeFormatter formatoPorDefecto) {
        Fecha.formatoPorDefecto = formatoPorDefecto;
    }

    public static Fecha getPresente() {
        return presente;
    }

    public static Fecha parsearFecha(String texto) {
        return texto != null ? new Fecha(texto) : null;
    }

    public Fecha() {
        super(LocalDate.now().atStartOfDay(), formatoPorDefecto);
    }

    public Fecha(String texto) {
        super(texto, formatoPorDefecto);
    }

    public Fecha(LocalDate localDate) {
        super(localDate.atStartOfDay(), formatoPorDefecto);
    }

    public Fecha(LocalDate localDate, DateTimeFormatter formato) {
        super(localDate.atStartOfDay(), formato);
    }

    public Fecha(String texto, String formato) {
        super(texto, DateTimeFormatter.ofPattern(formato));
    }

    public Fecha(int dia, int mes, int anno) {
        super(dia, mes, anno, 0, 0, 0, 0, formatoPorDefecto);
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
        super(fecha.localDateTime, formatoPorDefecto);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        return new Fecha(crearLocalDate(texto, formatter), formatter);
    }

    private void normalizar() {
        if (this.localDateTime != null) {
            this.localDateTime = localDateTime.truncatedTo(ChronoUnit.DAYS);
        }
    }

    @Override
    public boolean esPresente() {
        return this == presente;
    }

    public Fecha masDias(int dias) {
        if (esNula()) {
            return this;
        }
        LocalDate result = localDateTime.plusDays(dias).toLocalDate();
        return new Fecha(result, formato);
    }

    public Fecha masMeses(int meses) {
        if (esNula()) {
            return this;
        }
        LocalDate result = localDateTime.plusMonths(meses).toLocalDate();
        return new Fecha(result, formato);
    }
}
