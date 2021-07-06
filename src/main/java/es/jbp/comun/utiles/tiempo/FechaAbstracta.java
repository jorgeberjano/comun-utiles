package es.jbp.comun.utiles.tiempo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Clase base para Fecha y FechaHora
 *
 * @author jberjano
 */
public abstract class FechaAbstracta implements Serializable {
    protected LocalDateTime localDateTime;
    protected final DateTimeFormatter formato;
    protected static List<DateTimeFormatter> formatosAlternativos;
    static {
        formatosAlternativos = new ArrayList<>();

        formatosAlternativos.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS"));
        formatosAlternativos.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        formatosAlternativos.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        formatosAlternativos.add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        formatosAlternativos.add(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"));
        formatosAlternativos.add(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        formatosAlternativos.add(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        formatosAlternativos.add(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public FechaAbstracta(DateTimeFormatter formato) {
        this.formato = formato;
        localDateTime = LocalDateTime.now();
    }

    public FechaAbstracta(LocalDateTime localDateTime, DateTimeFormatter formato) {
        this.formato = formato;
        this.localDateTime = localDateTime;
    }

    public FechaAbstracta(Calendar calendar, DateTimeFormatter formato) {
        this.formato = formato;

        TimeZone tz = calendar.getTimeZone();
        ZoneId zoneId = tz.toZoneId();
        this.localDateTime = LocalDateTime.ofInstant(calendar.toInstant(), zoneId);
    }

    public FechaAbstracta(Date date, DateTimeFormatter formato) {
        this.formato = formato;
        localDateTime = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public FechaAbstracta(long milisegundos, DateTimeFormatter formato) {
        this.formato = formato;
        localDateTime = Instant.ofEpochMilli(milisegundos)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public FechaAbstracta(Fecha fecha, Hora hora, DateTimeFormatter formato) {
        this.formato = formato;
        localDateTime = LocalDateTime.from(fecha.getLocalDateTime());
    }

    public FechaAbstracta(String texto, DateTimeFormatter formato) {
        this.formato = formato;
        LocalDateTime candidato = crearLocalDateTime(texto, formato);
        if (candidato == null) {
            for (DateTimeFormatter otroFormato : formatosAlternativos) {
                candidato = crearLocalDateTime(texto, otroFormato);
            }
        }
        localDateTime = candidato;
    }

    public FechaAbstracta(int dia, int mes, int anno, int hora, int min, int seg, int ms, DateTimeFormatter formato) {
        this.formato = formato;
        localDateTime = LocalDateTime.of(anno, mes, dia, hora, min, seg, ms * 1000000);
    }

    protected static LocalDateTime crearLocalDateTime(String texto, DateTimeFormatter formato) {
        return LocalDateTime.parse(texto, formato);
    }

    protected static LocalDate crearLocalDate(String texto, DateTimeFormatter formato) {
        return LocalDate.parse(texto, formato);
    }

    public abstract boolean esPresente();

    protected final DateTimeFormatter getFormato() {
        return formato;
    }

    public boolean esValida() {
        return localDateTime != null;
    }

    public boolean esNula() {
        return localDateTime == null;
    }

    public boolean esAnteriorA(FechaAbstracta otraFecha) {
        if (esNula() || otraFecha == null || otraFecha.esNula()) {
            return false;
        }
        return localDateTime.isBefore(otraFecha.localDateTime);
    }

    public boolean esPosteriorA(FechaAbstracta otraFecha) {
        if (esNula() || otraFecha == null || otraFecha.esNula()) {
            return false;
        }
        return localDateTime.isAfter(otraFecha.localDateTime);
    }

    /**
     * Indica se la fecha coincide en día con otra fecha.
     *
     * @return
     */
    public boolean esMismoDia(FechaAbstracta otraFecha) {
        if (otraFecha == null) {
            return false;
        }
        return getDiaDelMes() == otraFecha.getDiaDelMes()
                && getMes() == otraFecha.getMes()
                && getAno() == otraFecha.getAno();
    }

    /**
     * Indica se la fecha coincide en día con otra fecha expresada con un objeto
     * LocalDateTime.
     */
    public boolean esMismoDia(LocalDateTime otroLocalDateTime) {
        if (esNula() || otroLocalDateTime == null) {
            return false;
        }

        return localDateTime.getDayOfMonth() == otroLocalDateTime.getDayOfMonth()
                && localDateTime.getMonth() == otroLocalDateTime.getMonth()
                && localDateTime.getYear() == otroLocalDateTime.getYear();
    }

    /**
     * Indica se la fecha es hoy.
     *
     * @return
     */
    public boolean esHoy() {
        return esMismoDia(LocalDateTime.now());
    }

    /**
     * Indica se la fecha es pasada.
     *
     * @return
     */
    public boolean esPasado() {
        if (esNula()) {
            return false;
        }
        return localDateTime.compareTo(LocalDateTime.now()) < 0;
    }

    /**
     * Indica se la fecha es futura.
     *
     * @return
     */
    public boolean esFuturo() {
        if (esNula()) {
            return false;
        }
        return localDateTime.compareTo(LocalDateTime.now()) > 0;
    }

    /**
     * Obtiene el día del mes (de 1 a 31)
     *
     * @return día del mes
     */
    public int getDiaDelMes() {
        if (esNula()) {
            return 0;
        }
        return localDateTime.getDayOfMonth();
    }

    /**
     * Obtiene la semana del mes (de 0 o 1 a 4 o 5, según el mes) La semana 1 se
     * cuenta desde el primer lunes, los dias de la semana cuyo lunes pertenece
     * al mes anterior tienen como semana la 0. No se puede usar
     * calendar.get(Calendar.WEEK_OF_MONTH) porque dicha función no cumple
     * exactamente este criterio.
     *
     * @return semana del mes
     */
    public int getSemanaDelMes() {
        if (esNula()) {
            return 0;
        }
        int diaMes = getDiaDelMes();
        int diaSemana = getDiaDeLaSemana();
        if (diaSemana > diaMes) {
            return 0;
        }
        return 1 + (diaMes - diaSemana) / 7;
    }

    /**
     * Obtiene el número del día de la semana. 1 - Lunes, 2 - Martes, 3 -
     * Miercoles, 4 - Jueves, 5 - Viernes, 6 - Sabado, 7 - Domingo
     *
     * @return número del día de la semana.
     */
    public int getDiaDeLaSemana() {
        if (esNula()) {
            return 0;
        }
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        return dayOfWeek.getValue();
    }

    /**
     * Obtiene el mes en formato numerico (1: Enero, 2: Febrero, ... 12:
     * Diciembre)
     *
     * @return
     */
    public int getMes() {
        return localDateTime.getMonthValue();
    }

    /**
     * Obtiene el año de la fecha
     *
     * @return
     */
    public int getAno() {
        return localDateTime.getYear();
    }

    public int diferenciaDias(FechaAbstracta otraFecha) {
        // Se hace el redondeo ya que, debido al cambio horario, hay un día al
        // año con 23 horas y otro con 25 horas
        return (int) Math.round(diferenciaMilisegundos(otraFecha) / (1000.0 * 60 * 60 * 24));
    }

    public int diferenciaHoras(FechaAbstracta otraFecha) {
        return (int) (diferenciaMilisegundos(otraFecha) / (1000 * 60 * 60));
    }

    public int diferenciaMinutos(FechaAbstracta otraFecha) {
        return (int) (diferenciaMilisegundos(otraFecha) / (1000 * 60));
    }

    public long diferenciaSegundos(FechaAbstracta otraFecha) {
        return diferenciaMilisegundos(otraFecha) / 1000;
    }

    public long diferenciaMilisegundos(FechaAbstracta otraFecha) {
        if (esNula()) {
            return 0;
        }
        return otraFecha.localDateTime.until(localDateTime, ChronoUnit.MILLIS);
    }

    public long toEpochMilli() {
        return LocalDateTime
                .of(1970, 1, 1, 0, 0, 0)
                .until(localDateTime, ChronoUnit.MILLIS);
    }

    @Override
    public boolean equals(Object obj) {
        if (esNula()) {
            return false;
        }
        if (!(obj instanceof FechaAbstracta)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        FechaAbstracta otraFecha = (FechaAbstracta) obj;
        return localDateTime.equals(otraFecha.localDateTime);
    }

    @Override
    public int hashCode() {
        if (esNula()) {
            return 0;
        }
        return Long.valueOf(toEpochMilli()).intValue();
    }

    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(localDateTime.getYear(), localDateTime.getMonthValue() - 1, localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());
        return calendar;
    }

    public Date toDate() {
        if (esNula()) {
            return null;
        }
        return java.util.Date
                .from(localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

//    public java.sql.Date toSqlDate() {
//        if (esNula()) {
//            return null;
//        }
//        return java.sql.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//    }

    public Timestamp toTimestamp() {
        if (esNula()) {
            return null;
        }
        return Timestamp.valueOf(localDateTime);
    }

    public final int compareTo(FechaAbstracta otraFecha) {
        boolean otraFechaEsNula = otraFecha == null || otraFecha.esNula();
        if (esNula() || otraFechaEsNula) {
            return esNula() && otraFechaEsNula? 0 : 1;
        }
        return localDateTime.compareTo(otraFecha.localDateTime);
    }

    public LocalDateTime getLocalDateTime() {
        if (esPresente()) {
            return LocalDateTime.now();
        }
        return localDateTime;
    }

    public String formatear(String formato) {
        if (formato == null) {
            return "";
        }
        return formatear(DateTimeFormatter.ofPattern(formato));
    }

    public String formatear(DateTimeFormatter formateador) {
        if (esNula() || formateador == null) {
            return "";
        } else {
            return formateador.format(localDateTime);
        }
    }

    @Override
    public String toString() {
        return formatear(formato);
    }

    public String getTexto() {
        return toString();
    }

    @Deprecated
    public Timestamp getTimestamp() {
        return Timestamp.valueOf(localDateTime);
    }

    public final Fecha getFecha() {
        return new Fecha(localDateTime.toLocalDate(), formato);
    }

    public final FechaHora getFechaHora() {
        return new FechaHora(localDateTime, formato);
    }

    public final FechaHoraMs getFechaHoraMs() {
        return new FechaHoraMs(localDateTime, formato);
    }

    public static long diferenciaEnMinutos(FechaHora fechaDesde, FechaHora fechaHasta) {
        return diferenciaEnMilisegundos(fechaDesde, fechaHasta) / 60000;
    }

    public static long diferenciaEnSegundos(FechaHora fechaDesde, FechaHora fechaHasta) {
        return diferenciaEnMilisegundos(fechaDesde, fechaHasta) / 1000;
    }

    public static long diferenciaEnMilisegundos(FechaHora fechaDesde, FechaHora fechaHasta) {
        long milisegundosFechaDesde = fechaDesde.toCalendar().getTimeInMillis();
        long milisegundosFechaActual = fechaHasta.toCalendar().getTimeInMillis();

        return Math.abs(milisegundosFechaDesde - milisegundosFechaActual);
    }

    public int getHoraDelDia() {
        if (esNula()) {
            return 0;
        }
        return localDateTime.getHour();
    }

    public int getMinuto() {
        if (esNula()) {
            return 0;
        }
        return localDateTime.getMinute();
    }

    public int getSegundo() {
        if (esNula()) {
            return 0;
        }
        return localDateTime.getSecond();
    }

    public Hora getHora() {
        return new Hora(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());
    }

    public java.sql.Date toSqlDate() {
        return java.sql.Date.valueOf(localDateTime.toLocalDate());
    }
}
