package es.jbp.comun.utiles.swing;

/**
 * Contrato que cumplen los objetos (normalmente paneles) que se pueden
 * aceptar o cancelar.
 * Se usan en los paneles incrustados en un diálogo de la clase
 * DialogoAceptarCancelar. El diálogo informa al panel a través de ésta
 * interface de eventos de aceptación o cancelación.
 *
 * @author Jorge Berjano
 */
public interface Aceptable {
    
    /**
     * Intenta aceptar el panel.
     * @return true si se permite que se se pueda aceptar.
     */
    boolean aceptar(); 
    
    /**
     * Intenta cerrar el panel.
     * @return true si se permite que se se pueda cancelar.
     */
    boolean cancelar();
    
}
