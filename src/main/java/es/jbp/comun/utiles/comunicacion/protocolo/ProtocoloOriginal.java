package es.jbp.comun.utiles.comunicacion.protocolo;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import es.jbp.comun.utiles.comunicacion.Comunicacion;

/**
 * Protocolo original de envio de tramas.
 * Se envia en formato de texto separando la clave del valor por un
 * caracter especial y cada par clave-valor por otro. 
 * Hay que evitar el uso de estos caracteres tanto en los nombres como en 
 * los valores de cadena.
 * Los buffers de bytes se codifican en base 64.
 * @author jberjano
 */
public class ProtocoloOriginal implements Protocolo {

    private final String separadorClaveValor = "~";
    private final String separadorEntradas = "|";
    private ByteArrayOutputStream constructorTramaEntrante;

    @Override
    public Trama byteRecibido(byte b) {
        if (constructorTramaEntrante == null) {
            if (b == Comunicacion.STX) {
                constructorTramaEntrante = new ByteArrayOutputStream();
            }
        } else if (b == Comunicacion.ETX) {
            Trama tramaRecibida = null;
            try {
                tramaRecibida = cadenaATrama(constructorTramaEntrante.toString("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                tramaRecibida = new Trama();
            }
            constructorTramaEntrante = null;
            return tramaRecibida;
        } else {
            constructorTramaEntrante.write(b);
        }
        return null;
    }

    public final Trama cadenaATrama(String texto) {
        Trama trama = new Trama();
        String[] entradas = texto.split("\\" + separadorEntradas);
        for (String entrada : entradas) {
            String claveValor[] = entrada.split("\\" + separadorClaveValor);
            if (claveValor.length >= 2) {
                trama.set(claveValor[0], claveValor[1]);
            }
        }
        return trama;
    }

    public String tramaACadena(Trama trama) {
        StringBuilder builder = new StringBuilder();
        for (String clave : trama.getClaves()) {
            String valor = trama.getString(clave);
            if (valor == null) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(separadorEntradas);
            }
            builder.append(clave);
            builder.append(separadorClaveValor);
            builder.append(valor);
        }
        return builder.toString();
    }

    @Override
    public byte[] getBytes(Trama trama) {

        try {
            byte[] bytes = tramaACadena(trama).getBytes("UTF-8");

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            out.write(Comunicacion.STX);
            out.write(bytes);
            out.write(Comunicacion.ETX);
            return out.toByteArray();
        } catch (Exception ex) {
            return null;
        }
    }

}
