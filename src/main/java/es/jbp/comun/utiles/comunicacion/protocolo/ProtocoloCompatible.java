package es.jbp.comun.utiles.comunicacion.protocolo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import es.jbp.comun.utiles.buffer.Buffer;
import es.jbp.comun.utiles.comunicacion.Comunicacion;
import es.jbp.comun.utiles.conversion.Conversion;

/**
 * Protocolo de envio de tramas compatible con la libreria UtilesQt.
 *
 * @author jberjano
 */
public class ProtocoloCompatible implements Protocolo {

    private final String CHARSET = "UTF-8";
    private ByteArrayOutputStream streamTrama;
    private int contador = 0;
    private long tamano;

    @Override
    public Trama byteRecibido(byte b) {
       
        if (streamTrama == null) {
            if (b == Comunicacion.STX) {
                // Inicio de trama
                streamTrama = new ByteArrayOutputStream();
                tamano = 0;
                contador = 1;
                return null;
            } else {
                // Caracter perdido
                return null;
            }
        }

        // Tamaño almacenado en los bytes del 1 al 4
        if (contador >= 1 && contador <= 4) {
            int valor = b & 0xFF;
            tamano += valor << (8 * (contador - 1));
            contador++;
            return null;
        }

        // EL TAMAÑO NO INCLUYE LOS 5 PRIMEROS BYTES
        if (contador >= tamano + 4) {
            Trama trama = null;            
            if (b == Comunicacion.ETX) {
                // Trama correcta
                trama = crearTrama(streamTrama.toByteArray());                
            } else {
                // Trama sin ETX
            }
            streamTrama = null;
            contador = 0;
            tamano = 0;
            return trama;
        }

        streamTrama.write(b);
        contador++;
        return null;
    }

    private String leerCadena(ByteArrayInputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch = in.read();
        while (in.available() > 0 && ch != 0) {
            out.write(ch);
            ch = in.read();
        }
        try {
            return new String(out.toByteArray(), CHARSET);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    private Trama crearTrama(byte[] bytes) {
        Trama trama = new Trama();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        int tipo = in.read();
        while (in.available() > 0) {
            String nombre = leerCadena(in);
            String valor = leerCadena(in);
            trama.set(nombre, valor);
            tipo = in.read();
        }
        return trama;
    }

    public Object convertirValor(Object valor, int tipo) {
        switch (tipo) {
            case 'B':
                return Conversion.toBoolean(valor);
            case 'I':
                return Conversion.toInteger(valor);
            case 'L':
                return Conversion.toLong(valor);
            case 'F':
                return Conversion.toDouble(valor);
            case 'R':
                return Conversion.toByteArray(valor);
            case 'S':
            default:
                return Conversion.toString(valor);
        }
    }

    public byte getTipo(Object valor) {
        if (valor instanceof Boolean) {
            return 'B';
        } else if (valor instanceof Integer || valor instanceof Short || valor instanceof Byte) {
            return 'I';
        } else if (valor instanceof Long) {
            return 'L';
        } else if (valor instanceof Double || valor instanceof Float) {
            return 'F';
        } else if (valor instanceof Buffer || valor instanceof byte[]) {
            return 'R';
        } else {
            return 'S';
        }
    }

    @Override
    public byte[] getBytes(Trama trama) {
        try {
            byte[] bytesValores = getBytesValores(trama);
            int longitud = bytesValores.length + 1;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(Comunicacion.STX);
            out.write(longitud & 0xFF);
            out.write((longitud >> 8) & 0xFF);
            out.write((longitud >> 16) & 0xFF);
            out.write((longitud >> 24) & 0xFF);
            out.write(bytesValores);
            out.write(Comunicacion.ETX);
            return out.toByteArray();
        } catch (Exception ex) {
            return null;
        }
    }

    protected byte[] getBytesValores(Trama trama) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (String clave : trama.getClaves()) {
            Object valor = trama.getObject(clave);
            String valorString = trama.getString(clave);
            if (valor == null) {
                continue;
            }
            out.write(getTipo(valor));
            out.write(clave.getBytes(CHARSET));
            out.write(0);
            out.write(valorString.getBytes(CHARSET));
            out.write(0);
        }

        return out.toByteArray();
    }
}
