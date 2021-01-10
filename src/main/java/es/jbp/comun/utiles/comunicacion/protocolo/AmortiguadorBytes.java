package es.jbp.comun.utiles.comunicacion.protocolo;

import java.io.ByteArrayInputStream;
import java.util.Queue;

/**
 * Permite escribir datos en arrays de bytes y leerlos byte a byte.
 *
 * @author jberjano
 */
public class AmortiguadorBytes {

    private Queue<byte[]> colaBytes;
    private ByteArrayInputStream in;

    public synchronized void escribr(byte[] bytes) {
        if (in == null) {
            in = new ByteArrayInputStream(bytes);
        } else {
            colaBytes.add(bytes);
        }
    }

    public synchronized int leerByte() {
        if (in.available() == 0) {
            in = null;
        }

        if (in == null) {
            byte[] bytes = colaBytes.poll();
            if (bytes == null) {
                in = null;
                return -1;
            }
            in = new ByteArrayInputStream(bytes);
        }
        
        return in.read();

    }
}
