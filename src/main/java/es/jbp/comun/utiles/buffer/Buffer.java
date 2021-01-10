package es.jbp.comun.utiles.buffer;

import java.util.Arrays;
import es.jbp.comun.utiles.conversion.Base64;

/**
 * Representa un buffer de memoria.
 * @author jberjano
 */
public class Buffer {
    private byte[] bytes;

    public Buffer() {
    }
    
    public Buffer(int longitud) {
        bytes = new byte[longitud];
    }

    public Buffer(byte[] bytes) {
        this.bytes = bytes;
    }
       
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    
    
    public void setByte(int i, byte valor) {
        if (bytes == null || i < 0 || i >= bytes.length) {
            return;
        }
        bytes[i] = valor;
    }
    
    public byte getByte(int i) {
        if (bytes == null || i < 0 || i >= bytes.length) {
            return 0;
        }
        return bytes[i];
    }
    
    public byte getPrimerByte() {
        return getByte(0);
    }

    public byte getUltimoByte() {
        return getByte(getLongitud() - 1);
    }
    
    private int getLongitud() {
        return bytes.length;
    }
    
    public String formatearABase64() {
        return Base64.encode(bytes);
    }
    public void parsearDeBase64(String texto) {
        bytes = Base64.decode(texto);
    }
    
    public String toString() {
        return formatearABase64();
    }    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Buffer other = (Buffer) obj;
        if (!Arrays.equals(this.bytes, other.bytes)) {
            return false;
        }
        return true;
    }

    
    
}
