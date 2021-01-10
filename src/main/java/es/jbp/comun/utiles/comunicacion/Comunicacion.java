package es.jbp.comun.utiles.comunicacion;

/**
 * Contrato que deben cumplir las clases que implementen comunicaciones.
 *
 * @author jberjano
 */
public interface Comunicacion {

    static final int STX = 0x02;
    static final int ETX = 0x03;

    interface Listener {

        void conectado();

        //void recepcion();
        void recibido(byte[] bytes);

        void error(String mensaje, Throwable ex);

        void desconectado();
    }

    void setTimeout(int timeout);

    boolean abrir(String nombre);

    boolean abrir();

    void cerrar();

    boolean estaAbierta();

    boolean enviar(byte[] bytes);

    boolean hayRecepcion();

    Byte recibirByte();

    byte[] recibir();

    void limpiarBufferEntrada();

    void procesarRecepcion();

    void setListener(Listener listener);

    boolean isForzarApertura();

    String getDireccion();
}
