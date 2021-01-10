package es.jbp.comun.utiles.hilos;

/**
 * Procesa en un hilo de ejecución paralela un proceso repetitivo con una
 * latencia o un objeto ejecutable.
 *
 * @author jberjano
 */
public class EjecutorAsincrono {

    protected boolean cancelado;
    protected int latencia = 200;
    protected Thread thread;
    protected String nombre;

    public interface Proceso {

        boolean procesar();
    }

    public interface Ejecutable {
        void ejecutar();
    }

    public EjecutorAsincrono() {
        nombre = "EjecutorAsincrono";
    }

    public EjecutorAsincrono(String nombre) {
        this.nombre = nombre;
    }

    public boolean ejecutar(final Ejecutable ejecutable) {

        // Si se llama desde el mismo thread simplemente se ejecuta
        if (Thread.currentThread() == thread) {
            ejecutable.ejecutar();
            return true;
        }
        
        if (isEjecutando()) {
            System.err.println(nombre + " " + ejecutable.toString() + ": ya hay un hilo de ejecución");
            return false;
        }
        setCancelado(false);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ejecutable.ejecutar();
            }
        });
        thread.setName(nombre + ": " + ejecutable.toString());
        thread.start();
        return true;
    }

    public boolean ejecutar(Proceso proceso) {
        return ejecutarHilo(proceso);
    }

    public boolean isEjecutando() {
        return thread != null && thread.isAlive();
    }

    public synchronized void setCancelado(boolean b) {
        cancelado = b;
    }

    public synchronized boolean isCancelado() {
        return cancelado;
    }

    public void cancelarYEsperar() {
        if (thread == null) {
            return;
        }
        setCancelado(true);
        try {
            if (Thread.currentThread() != thread) {
                thread.join();
            }
        } catch (InterruptedException ex) {
        }
    }

    public int getLatencia() {
        return latencia;
    }

    public void setLatencia(int latencia) {
        this.latencia = latencia;
    }

    private boolean ejecutarHilo(final Proceso proceso) {

        if (proceso == null) {
            return false;
        }

        setCancelado(false);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isCancelado()) {
                    boolean continuar = proceso.procesar();
                    if (!continuar) {
                        break;
                    }
                    pausa(latencia);
                }
            }
        });
        thread.setName(nombre + " " + proceso.toString());
        thread.start();
        return true;
    }

    public boolean pausa(long milisegundos) {
        long decisegundos = milisegundos / 100;
        long restoMs = milisegundos % 100;
        for (int i = 0; i < decisegundos; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                return false;
            }
            if (isCancelado()) {
                return false;
            }
        }
        if (restoMs > 0) {
            try {
                Thread.sleep(restoMs);
            } catch (InterruptedException ex) {
                return false;
            }
        }
        return true;
    }
}
