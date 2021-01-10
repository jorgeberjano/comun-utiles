package es.jbp.comun.utiles.depuracion;

import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import es.jbp.comun.utiles.tiempo.FechaHora;

/**
 * Gestor de mensajes de log. Gestiona tanto los mensajes de error como los de 
 * traza.
 * @author jberjano
 */
public class GestorLog {

    private static Logger logger;   
    private static boolean mostrarTraza;
    private static boolean salidaPorConsola = true;
    
    private static Listener listener;
    
    public interface Listener {
        void error(String mensaje, Throwable ex);
        void traza(String mensaje);
    }

    private GestorLog() {
    }   

    public static void setListener(Listener listener) {
        GestorLog.listener = listener;
    }
    
    public static void inicializar(Logger log) {
        logger = log;
    }
   
    public static void inicializar(String nombre) {
                
        logger = Logger.getLogger(nombre);
        
        FileHandler logFileHandle;
        try {
            logFileHandle = new FileHandler(nombre + ".log");
        } catch (Exception ex) {
            return;
        }
        logFileHandle.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                FechaHora fechaHora = new FechaHora(record.getMillis());
                
                return fechaHora.toString() + " - "
                    + record.getMessage() + "\n";
            }
        });
        logger.addHandler(logFileHandle);
        logger.setLevel(Level.FINE);
    }

    public static boolean isMostrarTraza() {
        return mostrarTraza;
    }

    public static void setMostrarTraza(boolean mostrarTraza) {
        GestorLog.mostrarTraza = mostrarTraza;
    }

    public static boolean isSalidaPorConsola() {
        return salidaPorConsola;
    }

    public static void setSalidaPorConsola(boolean salidaPorConsola) {
        GestorLog.salidaPorConsola = salidaPorConsola;
    }
    
    public static void error(String mensaje, Throwable ex) {    
        
        if (listener != null) {
            listener.error(mensaje, ex);
        }
        
        if (ex != null) {
            if (ex.getMessage() != null) {
                mensaje += ". Causa: " + ex.getMessage();
            } else {
                mensaje += ". Causa: " + ex.getClass().getSimpleName();
            }
        }
        if (logger != null) {
            logger.severe(mensaje);
        }
        if (salidaPorConsola) {
            System.err.println(mensaje);
        }
    }
    
    public static void traza(String mensaje) {      
        
        if (listener != null) {
            listener.traza(mensaje);
        }
        
        if (!mostrarTraza) {
            return;
        }
        if (logger != null) {
            logger.fine(mensaje);
        }
        if (salidaPorConsola) {
             System.out.println(mensaje);
        }
    }
}
