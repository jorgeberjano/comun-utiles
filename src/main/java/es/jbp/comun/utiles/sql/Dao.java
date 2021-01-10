package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import java.io.IOException;
import java.net.URL;

/**
 * Clase base para los objetos de acceso a datos.
 *
 * @author jberjano
 */
public abstract class Dao {

    protected GestorConexiones gestorConexiones;
    protected String mensajeError;
    protected String causaError;

    private static Listener listener;
    private static boolean trazaSoloErrores = true;

    public interface Listener {

        void error(String mensaje, Exception ex);

        void trazaSql(String sql);
    }

    public static void setListener(Listener listener) {
        Dao.listener = listener;
    }

    public static void setTrazaSoloErrores(boolean trazaSoloErrores) {
        Dao.trazaSoloErrores = trazaSoloErrores;
    }

    public Dao(GestorConexiones gestorConexiones) {
        this.gestorConexiones = gestorConexiones;
    }

    protected void reportarExcepcion(final String mensaje, final Exception ex) {
        if (listener != null) {
            listener.error(mensaje, ex);
        }
        mensajeError = mensaje;
        if (ex != null) {
            causaError = ex.getMessage();
        }
    }

    protected static void trazaSql(final String sql, boolean exito) {
        if (listener == null) {
            return;
        }
        if (trazaSoloErrores && exito) {
            return;
        }
        listener.trazaSql(sql);
    }

    public boolean huboError() {
        return mensajeError != null;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public String getCausaError() {
        return causaError;
    }

    public String getMensajeCompletoError() {
        StringBuilder builder = new StringBuilder();
        if (mensajeError != null) {
            builder.append(mensajeError);
        }
        if (mensajeError != null && causaError != null) {
            builder.append(". Causa: ");
        }
        if (causaError != null) {
            builder.append(causaError);
        }
        return builder.toString();
    }

    /**
     * Limpia el mensaje de error.
     */
    protected void limpiarMensajeError() {
        mensajeError = null;
        causaError = null;
    }

    protected final FormateadorSql getFormateadorSql() {
        return gestorConexiones.getFormateadorSql();
    }

    protected EjecutorSentenciaSelect crearEjecutorSentenciaSelect() {
        return new EjecutorSentenciaSelect(gestorConexiones);
    }

    protected EjecutorSentenciaInsert crearEjecutorSentenciaInsert() {
        return new EjecutorSentenciaInsert(gestorConexiones);
    }

    protected EjecutorSentenciaUpdate crearEjecutorSentenciaUpdate() {
        return new EjecutorSentenciaUpdate(gestorConexiones);
    }

    protected EjecutorSentenciaDelete crearEjecutorSentenciaDelete() {
        return new EjecutorSentenciaDelete(gestorConexiones);
    }

    protected EjecutorSentenciaSimple crearEjecutorSentenciaSimple() {
        return new EjecutorSentenciaSimple(gestorConexiones);
    }
    
    public PlantillaSql crearPlantillaSql(String sql) {
        return new PlantillaSql(sql, gestorConexiones.getFormateadorSql());
    }
    
    public PlantillaSql crearPlantillaSql(URL url) throws IOException {
        return new PlantillaSql(url, gestorConexiones.getFormateadorSql());
    }

}
