package es.jbp.comun.utiles.tiempo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Clase base para Fecha y FechaHora
 *
 * @author jberjano
 */
public abstract class FechaAbstracta implements Serializable {

    protected final Calendar calendar;
    protected final DateFormat formato;
    protected static List<DateFormat> formatosAlternativos;
    static {
        formatosAlternativos = new ArrayList<>();
        formatosAlternativos.add(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS"));
        formatosAlternativos.add(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
        formatosAlternativos.add(new SimpleDateFormat("dd/MM/yyyy HH:mm"));
        formatosAlternativos.add(new SimpleDateFormat("dd/MM/yyyy"));
        formatosAlternativos.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS"));
        formatosAlternativos.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        formatosAlternativos.add(new SimpleDateFormat("dd-MM-yyyy HH:mm"));
        formatosAlternativos.add(new SimpleDateFormat("dd-MM-yyyy"));
    }

    public FechaAbstracta(DateFormat formato) {
        this.formato = formato;
        calendar = Calendar.getInstance();
    }

    public FechaAbstracta(Calendar calendar, DateFormat formato) {
        this.formato = formato;
        this.calendar = (Calendar) calendar.clone();
    }

    public FechaAbstracta(Date date, DateFormat formato) {
        this.formato = formato;
        if (date == null) {
            this.calendar = null;
        } else {
            this.calendar = Calendar.getInstance();
            this.calendar.setTime(date);
        }
    }

    public FechaAbstracta(long milisegundos, DateFormat formato) {
        this.formato = formato;
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeInMillis(milisegundos);
    }

    public FechaAbstracta(Fecha fecha, Hora hora, DateFormat formato) {
        this.formato = formato;
        calendar = fecha.calendar;
        calendar.set(Calendar.HOUR_OF_DAY, hora.getHora());
        calendar.set(Calendar.MINUTE, hora.getMinuto());
        calendar.set(Calendar.SECOND, hora.getSegundo());
    }

    public FechaAbstracta(String texto, DateFormat formato) {
        this.formato = formato;
        Calendar calendarCandidato = crearCalendar(texto, formato);
        if (calendarCandidato != null) {
            calendar = calendarCandidato;
            return;
        }
        for (DateFormat otroFormato : formatosAlternativos) {
            calendarCandidato = crearCalendar(texto, otroFormato);
            if (calendarCandidato != null) {
                calendar = calendarCandidato;
                return;
            }
        }
        calendar = null;
    }

    public FechaAbstracta(int dia, int mes, int anno, int hora, int min, int seg, int ms, DateFormat formato) {
        this.formato = formato;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, anno);
        calendar.set(Calendar.MONTH, mes - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, seg);
        calendar.set(Calendar.MILLISECOND, ms);
    }

    protected static Calendar crearCalendar(String texto, DateFormat formato) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formato.parse(texto));
        } catch (Throwable ex) {
            calendar = null;
        }
        return calendar;
    }

    public abstract boolean esPresente();

    protected final DateFormat getFormato() {
        return formato;
    }

    public boolean esValida() {
        return calendar != null;
    }

    public boolean esNula() {
        return calendar == null;
    }

    public boolean esAnteriorA(FechaAbstracta otraFecha) {
        if (esNula() || otraFecha == null || otraFecha.esNula()) {
            return false;
        }
        return calendar.before(otraFecha.calendar);
    }

    public boolean esPosteriorA(FechaAbstracta otraFecha) {
        if (esNula() || otraFecha == null || otraFecha.esNula()) {
            return false;
        }
        return calendar.after(otraFecha.calendar);
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
        return esMismoDia(otraFecha.calendar);
    }

    /**
     * Indica se la fecha coincide en día con otra fecha expresada con un objeto
     * calendar.
     *
     * @return
     */
    public boolean esMismoDia(Calendar otroCalendar) {
        if (esNula() || otroCalendar == null) {
            return false;
        }

        return calendar.get(Calendar.DATE) == otroCalendar.get(Calendar.DATE)
                && calendar.get(Calendar.MONTH) == otroCalendar.get(Calendar.MONTH)
                && calendar.get(Calendar.YEAR) == otroCalendar.get(Calendar.YEAR);
    }

    /**
     * Indica se la fecha es hoy.
     *
     * @return
     */
    public boolean esHoy() {
        return esMismoDia(Calendar.getInstance());
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
        return calendar.compareTo(Calendar.getInstance()) < 0;
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
        return calendar.compareTo(Calendar.getInstance()) > 0;
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
        return calendar.get(Calendar.DAY_OF_MONTH);
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
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == 1 ? 7 : dayOfWeek - 1;
    }

    /**
     * Obtiene el mes en formato numerico (1: Enero, 2: Febrero, ... 12:
     * Diciembre)
     *
     * @return
     */
    public int getMes() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * Obtiene el año de la fecha
     *
     * @return
     */
    public int getAno() {
        return calendar.get(Calendar.YEAR);
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
        long diff = calendar.getTime().getTime() - otraFecha.calendar.getTime().getTime();

        return diff;
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
        return calendar.equals(otraFecha.calendar);
    }

    @Override
    public int hashCode() {
        if (esNula()) {
            return 0;
        }
        return (int) this.calendar.getTimeInMillis();
    }

    public Calendar toCalendar() {
        return calendar;
    }

    public Date toDate() {
        if (esNula()) {
            return null;
        }
        return calendar.getTime();
    }

    public java.sql.Date toSqlDate() {
        if (esNula()) {
            return null;
        }
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    public Timestamp toTimestamp() {
        if (esNula()) {
            return null;
        }
        return new Timestamp(calendar.getTimeInMillis());
    }

    public final int compareTo(FechaAbstracta otraFecha) {
        boolean otraFechaEsNula = otraFecha == null || otraFecha.esNula();
        if (esNula() || otraFechaEsNula) {
            return esNula() && otraFechaEsNula? 0 : 1;
        }
        return calendar.compareTo(otraFecha.calendar);
    }

    public Calendar getCalendar() {
        if (esPresente()) {
            return Calendar.getInstance();
        }
        return (Calendar) calendar.clone();
    }

    public String formatear(String formato) {
        if (formato == null) {
            return "";
        }
        return formatear(new SimpleDateFormat(formato));
    }

    public String formatear(DateFormat formateador) {
        if (esNula() || formateador == null) {
            return "";
        } else {
            return formateador.format(calendar.getTime());
        }
    }

    @Override
    public String toString() {
        return formatear(formato);
    }

    public String getTexto() {
        return toString();
    }

    public Timestamp getTimestamp() {
        return new Timestamp(calendar.getTimeInMillis());
    }

    public final Fecha getFecha() {
        return new Fecha(calendar);
    }

    public final FechaHora getFechaHora() {
        return new FechaHora(calendar);
    }

    public final FechaHoraMs getFechaHoraMs() {
        return new FechaHoraMs(calendar);
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
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinuto() {
        if (esNula()) {
            return 0;
        }
        return calendar.get(Calendar.MINUTE);
    }

    public int getSegundo() {
        if (esNula()) {
            return 0;
        }
        return calendar.get(Calendar.SECOND);
    }

    public Hora getHora() {
        return new Hora(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }
}
