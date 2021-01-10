package es.jbp.comun.utiles.comunicacion.protocolo;

import es.jbp.comun.utiles.comunicacion.Comunicacion;
import es.jbp.comun.utiles.comunicacion.ProcesoComunicacion;
import es.jbp.comun.utiles.tiempo.Cronometro;

/**
 * Comunicador de alto nivel que permite enviar tramas.
 * @author jberjano
 */
public class Comunicador {

    private Comunicacion comunicacion;
    private Listener listener;
    private ProcesoComunicacion proceso;
    private Protocolo protocolo = new ProtocoloCompatible();
    private boolean recepcionAbortada;

    public interface Listener {

        void conectado();

        void tramaRecibida(Trama trama);

        void error(String mensaje, Throwable ex);

        void desconectado();
    }

    public Comunicador() {
    }

    public Comunicador(Comunicacion comunicacion) {
        this.comunicacion = comunicacion;
    }

    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }
    
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void iniciar(Comunicacion comunicacion) {
        this.comunicacion = comunicacion;
        iniciar();
    }

    public void iniciar() {
        if (comunicacion == null) {
            return;
        }

        comunicacion.setListener(new Comunicacion.Listener() {

            @Override
            public void conectado() {
                if (listener != null) {
                    listener.conectado();
                }
            }

            @Override
            public void recibido(byte[] bytes) {
                procesarRecepcion(bytes);
            }

            @Override
            public void error(String mensaje, Throwable ex) {
                if (listener != null) {
                    listener.error(mensaje, ex);
                }
            }

            @Override
            public void desconectado() {
                if (listener != null) {
                    listener.desconectado();
                }
            }
        });
        proceso = new ProcesoComunicacion(comunicacion);
        proceso.iniciar();
    }

    public void finalizar() {
        if (proceso != null) {
            proceso.finalizar();
        }
    }
    
    public boolean enviar(Trama trama) {

        byte[] bytes = protocolo.getBytes(trama);
        return enviar(bytes);
    }
    
    public boolean enviar(byte[] bytes) {
        if (comunicacion == null) {
            return false;
        }                  
        if (comunicacion.isForzarApertura() && !comunicacion.estaAbierta()) {
            boolean ok = comunicacion.abrir();
            if (!ok) {
                return false;
            }
        }            
        return comunicacion.enviar(bytes);
    }

    public Trama esperarTrama(int timeout) {

        recepcionAbortada = false;
                
        if (!comunicacion.estaAbierta()) {
            return null;
        }

        Cronometro crono = new Cronometro();
        while (timeout == 0 || crono.getMilisegundosTranscurridos() < timeout) {
            if (recepcionAbortada || !comunicacion.estaAbierta()) {
                break;
            }
            Byte b = comunicacion.recibirByte();
            if (b == null) {
                continue;
            }
            Trama trama = protocolo.byteRecibido(b);
            if (trama != null) {
                return trama;
            }
        }
        return null;
    }   

    private void procesarRecepcion(byte[] bytes) {
        if (bytes == null) {
            return;
        }
        for (byte b : bytes) {
            Trama trama = protocolo.byteRecibido(b);
            if (trama != null && listener != null) {
                listener.tramaRecibida(trama);
            }
        }
    }

    public boolean isComunicacionAbierta() {
        return comunicacion.estaAbierta();
    }

    public String getDireccion() {
        return comunicacion.getDireccion();
    }

    public void abortarRecepcion() {
        recepcionAbortada = true;
    }
    
    public void cerrarComumicacion() {
        comunicacion.cerrar();
    }

}
