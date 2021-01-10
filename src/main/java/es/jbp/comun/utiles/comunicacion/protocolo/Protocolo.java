package es.jbp.comun.utiles.comunicacion.protocolo;

/**
 * Protocolo que implementa tanto la construcción de tramas recibiendo byte a byte.
 * Si se completa la trama la devuelve.
 * En caso contrario devuelve null.
 * @author jberjano
 */
public interface Protocolo {
    /**
     * Construye una trama recibiendo byte a byte. 
     * Si la trama de completa se completa se devuelve como valor de retorno.
     * En caso contrario se devuelve null.
     * @param b Byte recibido
     * @return Trama completada o null
     */
    public Trama byteRecibido(byte b);
    
    /**
     * Serializa una trama en bytes.
     * @param trama La trama a serializar
     * @return la trama como una secuencia de bytes
     */
    byte[] getBytes(Trama trama);
}
