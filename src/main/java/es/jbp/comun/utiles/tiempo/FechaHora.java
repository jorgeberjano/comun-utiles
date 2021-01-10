package es.jbp.comun.utiles.tiempo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private static DateFormat formatoPorDefecto;

//    public class Deserializador extends JsonDeserializer<FechaHora> {
//
//        @Override
//        public FechaHora deserialize(JsonParser jp, DeserializationContext dc)
//                throws IOException, JsonProcessingException {            
//            return new FechaHora(dc.readValue(jp, String.class));
//        }
//        
//    }
//    public class Serializador extends JsonSerializer<FechaHora> {
//
//            @Override
//            public void serialize(FechaHora value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//                jgen.writeString(value.toString());
//            }            
//        }

    static {
        formatoPorDefecto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        presente = new FechaHora();
    }

    public static DateFormat getFormatoPorDefecto() {
        return FechaHora.formatoPorDefecto;
    }

    public static void setFormatoPorDefecto(DateFormat formatoPorDefecto) {
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
        return new FechaHora(crearCalendar(texto, new SimpleDateFormat(formato)));
    }

    public FechaHora() {
        super(Calendar.getInstance(), formatoPorDefecto);
        normalizar();
    }

    public FechaHora(FechaAbstracta fecha) {
        super(fecha.calendar, formatoPorDefecto);
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
        super(texto, new SimpleDateFormat(formato));
        normalizar();
    }

    private void normalizar() {
        if (this.calendar != null) {
            this.calendar.set(Calendar.MILLISECOND, 0);
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
        Calendar nuevoCalendar = (Calendar) calendar.clone();
        nuevoCalendar.add(Calendar.HOUR_OF_DAY, horas);
        return new FechaHora(nuevoCalendar);
    }

    public FechaHora masMinutos(int minutos) {
        if (esNula()) {
            return this;
        }
        Calendar nuevoCalendar = (Calendar) calendar.clone();
        nuevoCalendar.add(Calendar.MINUTE, minutos);
        return new FechaHora(nuevoCalendar);
    }

    public FechaHora masSegundos(int segundos) {
        if (esNula()) {
            return this;
        }
        Calendar nuevoCalendar = (Calendar) calendar.clone();
        nuevoCalendar.add(Calendar.SECOND, segundos);
        return new FechaHora(nuevoCalendar);
    }
}
