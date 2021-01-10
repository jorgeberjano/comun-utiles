package es.jbp.comun.utiles.comunicacion;

import es.jbp.comun.utiles.hilos.EjecutorAsincrono;

/**
 * Proceso que se repite con una latencia determinada. Comprueba periodicamente 
 * si se produce alguna recepción a traves de la comunicación.
 * @author jberjano
 */
public class ProcesoComunicacion implements EjecutorAsincrono.Proceso {

    private final Comunicacion comunicacion;
    private EjecutorAsincrono ejecutor;   
    private String nombre = "0.0.0.0";
    
    public ProcesoComunicacion(Comunicacion comunicacion) {
        this.comunicacion = comunicacion;        
    }
    
    public void iniciar() {        
        if (ejecutor != null) {
            return;
        }
        
        ejecutor = new EjecutorAsincrono();
        ejecutor.ejecutar(this);
    }
    
    public void finalizar() {
        if (ejecutor == null) {
            return;
        }
        ejecutor.cancelarYEsperar();
        ejecutor = null;        
    }   
   
    @Override
    public boolean procesar() {
        if (comunicacion == null) {
            return false;
        }
        
        if (comunicacion.estaAbierta()) {
            comunicacion.procesarRecepcion();
        } else if (comunicacion.isForzarApertura()) {
            comunicacion.abrir();
        } else {
            return false;
        }
        return true;
    }   

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Proceso de comunicacion " + nombre;
    }

}
