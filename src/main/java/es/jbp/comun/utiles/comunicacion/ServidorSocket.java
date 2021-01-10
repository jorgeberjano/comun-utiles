package es.jbp.comun.utiles.comunicacion;

import es.jbp.comun.utiles.hilos.EjecutorAsincrono;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Implementa un servidor de sockets.
 * @author jberjano
 */
public class ServidorSocket implements EjecutorAsincrono.Proceso {

    private ServerSocket serverSocket;
    private int timeout = 1000;
    private Listener listener;
    private EjecutorAsincrono ejecutor;
    private String nombre;

    public ServidorSocket() {
        
    }
    
    public String toString() {
        return nombre;
    }
    
    public interface Listener {
        void conectado(Socket socket);
        void error(String mensaje, Throwable ex);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean iniciar(int puerto) {

        if (ejecutor != null) {
            return false;
        }
        nombre = "Puerto "+ puerto;
        
        try {
            ejecutor = new EjecutorAsincrono("Servidor sockets");
            serverSocket = new ServerSocket(puerto);
        } catch (Exception ex) {
            if (listener != null) {
                listener.error("No se ha podido crear el socket servidor en el puerto " + puerto, ex);
            }
            return false;
        }
        ejecutor.ejecutar(this);
        return true;
    }

    public void finalizar() {
        ejecutor.cancelarYEsperar();
        ejecutor = null;
    }


    @Override
    public boolean procesar() {
 
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();

            if (clientSocket != null) {
                clientSocket.setSoTimeout(timeout);
                if (listener != null) {
                    listener.conectado(clientSocket);
                }                
            }
        } catch (IOException ex) {
            listener.error("No se ha podido aceptar el socket cliente en el puerto " + serverSocket.getLocalPort(), ex);
        }     
        return true; // nunca termina
     }
}
