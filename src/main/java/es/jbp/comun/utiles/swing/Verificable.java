package es.jbp.comun.utiles.swing;

/**
 * Contrato que cumplen los objetos (normalmente paneles) que pueden verificar
 * si se pueden aceptar o cancelar.
 * @author Jorge Berjano
 */
public interface Verificable {
    /**
     * Verifica si se puede aceptar.
     */
    boolean verificarAceptar();
    
    /**
     * Verifica si se puede cancelar.
     */
    boolean verificarCancelar();
}
