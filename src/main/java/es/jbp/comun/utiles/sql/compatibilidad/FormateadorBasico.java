package es.jbp.comun.utiles.sql.compatibilidad;

import java.util.HashMap;
import java.util.Map;
import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.sql.TipoDato;
import static es.jbp.comun.utiles.sql.TipoDato.BOOLEANO;
import static es.jbp.comun.utiles.sql.TipoDato.CADENA;
import static es.jbp.comun.utiles.sql.TipoDato.ENTERO;
import static es.jbp.comun.utiles.sql.TipoDato.FECHA;
import static es.jbp.comun.utiles.sql.TipoDato.FECHA_HORA;
import static es.jbp.comun.utiles.sql.TipoDato.REAL;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaHora;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Formateador SQL con comportamiento basico.
 *
 * @author jberjano
 */
public abstract class FormateadorBasico implements FormateadorSql {

    protected final Map<String, String> mapaSimbolos = new HashMap<>();

    @Override
    public String formatear(Object valor) {
        if (valor == null) {
            return "null";
        } else if (valor instanceof String) {
            return "'" + ((String) valor).replace("'", "''") + "'";
        } else if (valor instanceof Boolean) {
            return getBooleanSql((Boolean) valor);
        } else if (valor instanceof Fecha) {
            return getFechaSql((Fecha) valor);
        } else if (valor instanceof FechaHora) {
            return getFechaHoraSql((FechaHora) valor);
        } else if (valor instanceof FechaHoraMs) {
            return getFechaHoraMsSql((FechaHoraMs) valor);
        }else {
            return Conversion.toString(valor);
        }
    }

    @Override
    public String getValorSimbolo(String simbolo) {
        return mapaSimbolos.get(simbolo.toUpperCase());
    }

    @Override
    public Set<Integer> getCodigosErrorDesconexion() {
        return new HashSet<>();
    }

    @Override
    public Object convertirAlPersistir(Object valor) {
        if (valor instanceof Boolean) {
            return (Boolean) valor ? 1 : 0;
        }
        return valor;
    }

    @Override
    public String getContieneTexto(String valor) {
        return  getCaracterComodin() + valor +  getCaracterComodin();
    }

    @Override
    public String getCaracterComodin() {
        return "%";
    }

    @Override
    public String getOperadorDistinto() {
        return "<>";
    }

    @Override
    public String getComparacionNoCase(String operador1, String operando, String operador2) {
        return new StringBuilder()
                .append(operador1)
                .append(' ')
                .append(operando)
                .append(' ')
                .append(operador2)
                .toString();
    }
        
    @Override
    public Fecha toFecha(Object valor) {
        return Conversion.toFecha(valor);
    }
    @Override
    public FechaHora toFechaHora(Object valor) {
        return Conversion.toFechaHora(valor);
    }
    
    @Override
    public FechaHoraMs toFechaHoraMs(Object valor) {
        return Conversion.toFechaHoraMs(valor);
    }
    
    public Object convertirValor(Object valor, TipoDato tipoDato) {
        switch (tipoDato) {
            case CADENA:
                return toString(valor);
            case ENTERO:
                return Conversion.toLong(valor);
            case REAL:
                return Conversion.toDouble(valor);
            case BOOLEANO:
                return Conversion.toBoolean(valor);
            case FECHA:
                return Conversion.toFecha(valor);
            case FECHA_HORA:
                FechaHora fechaHora = Conversion.toFechaHora(valor);
                return fechaHora != null ? fechaHora : Conversion.toFecha(valor);
            case BYTES:
                return toByteArray(valor);
            default:
                return null;
        }
    }       
    
    public static String toString(Object valor) {
        if (valor == null) {
            return null;
        } else if (valor instanceof String) {
            return (String) valor;
        } else if (valor instanceof byte[]) {
            try {
                return new String((byte[]) valor, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                return null;
            }
        } else {
            return valor.toString();
        }
    }
    
    private static byte[] toByteArray(Object valor) {
        if (valor == null) {
            return null;
        } else if (valor instanceof byte[]) {
            return (byte[]) valor;
        } else try {
            return valor.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }        
    }

    @Override
    public String getNombreParametro(int indice) {
        return "$" + (indice + 1);
    }
}
