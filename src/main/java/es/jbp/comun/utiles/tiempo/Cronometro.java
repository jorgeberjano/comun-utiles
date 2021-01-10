package es.jbp.comun.utiles.tiempo;

/**
 * Implementa la funcionalidad de un cronómetro para medir distancias de tiempo.
 * @author jberjano
 */
public class Cronometro {
    private long inicio;    
    private long nanosegundosExpiracion;
    
    public Cronometro() {
        inicio();
    }
    
    public final void inicio() {
       inicio = System.nanoTime();
    }
    
    public long getSegundosTranscurridos() {       
        return getNanosegundosTranscurridos() / 1000000000;
    }
    
    public long getMilisegundosTranscurridos() {       
        return getNanosegundosTranscurridos() / 1000000;
    }
    
    public long getNanosegundosTranscurridos() {
        
        long ahora = System.nanoTime();
        long transcurrido = ahora - inicio;
        return transcurrido;
    }      
    
    public void getNanosegundosExpiracion(long ns) {
        nanosegundosExpiracion = ns;
    } 
    
    public void getMilisegundosExpiracion(long ms) {
        nanosegundosExpiracion = ms * 1000000;
    } 

    public void getSegundosExpiracion(long seg) {
        nanosegundosExpiracion = seg * 1000000000;
    } 
    
    public boolean haExpirado() {
        return getNanosegundosTranscurridos() >= nanosegundosExpiracion;
    }
    
    public void forzarExpiracion() {
        inicio = System.nanoTime() - nanosegundosExpiracion;
    }
}
