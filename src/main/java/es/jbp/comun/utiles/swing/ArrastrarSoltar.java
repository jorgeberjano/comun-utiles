package es.jbp.comun.utiles.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.List;

/*
    Pasos para crear el efecto drag:
    1.1. Crear un DragSource.
    1.2. Pedir que el DragSource vigile componentes específicos para
    funcionalidades de arrastrado.
    1.3. Registrar una clase con el DragSource para ser notificado cuando
    un arrastre es reconocido (a través de la interfaz
    java.awt.dnd.DragGestureListener).
    1.4. Crear un objeto que implemente java.awt.datatransfer.Transferable
    y poner en él la información que queremos permitir que sea arrastrada.
    1.5. Informar al DragSource que comienze la operación de arrastre utilizando
    el método startDrag().
    1.6. Escuchar por eventos que ocurren mientras que el objeto Transferable es
    arrastrado por sobre un objeto "dropable".
    1.7. Responder a un drop.
    Pasos para crear el efecto drop:
    2.1. Crear un DropTarget.
    2.2. Pedir que el DropTarget observe componentes específicos para objetos que
    están siendo arrastrados.
    2.3. Pedir que el DropTarget notifique una clase específica cuando un objeto
    está siendo arrastrado sobre un componente, y cuando es soltado (dropped),
    a través de la interfaz java.awt.dnd.DropTargetListener.
    2.4. Manipular las notificaciones cuando un objeto es arrastrado sobre nuestro
    componente.
    2.5. En el evento de soltar decidir si se acepta o no el objeto.
    2.6: Se estraen los datos del objeto transferido y se usan.
    2.7. Completar el drop (notificar al DragSource).
 */

/**
 * Clase abstracta que facilita la implementación del mecanismo de arrastrar desde 
 * y soltar a un componente swing.
 * @author Jorge Berjano
 */
public abstract class ArrastrarSoltar 
    implements DragSourceListener, DragGestureListener, DropTargetListener {
    
    private DragSource dragSource = null; // Permite ser fuente para arrastrar
    private DropTarget dropTarget = null; // Permite ser destino para arrojar
    private String nombreSabor;
    private Component componente;

    public ArrastrarSoltar(Component componente, String nombreSabor) {
        this.componente = componente;
        this.nombreSabor = nombreSabor;    
        
        // Paso 1.1
        dragSource = new DragSource();

        // Pasos 1.2 y 1.3
        dragSource.createDefaultDragGestureRecognizer(componente, accionesPermitidas(), this);

        // Pasos 2.1, 2.2 y 2.3
        dropTarget = new DropTarget(componente, this);        
    }
    
    public int accionesPermitidas() { return DnDConstants.ACTION_COPY_OR_MOVE; }

    public Component getComponente() {
        return componente;
    }
            
    private class ObjetoTransferible implements Transferable {
        private Point posicion;
        
        public ObjetoTransferible(Point posicion) {
            this.posicion = posicion;
        }

        /**
         * Devuelve un array de un elemento con el sabor que soporta el objeto transferible.
         */
        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] sabores = new DataFlavor[1];
            sabores[0] = new DataFlavor(List.class, nombreSabor);
            return sabores;
        }

        /**
         * Devuelve true si el sabor es el que soporta el objeto transferible.
         */
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.getHumanPresentableName().equals(nombreSabor);
        }

        /**
         * Obtene el objeto dato que se trasnfiere a partir de un sabor.
         */
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            Object transferData = null;
            if (flavor.getHumanPresentableName().equals(nombreSabor)) {
                transferData = crearDatoTransferible(posicion);
            }
            return transferData;
        }
    }
    
    /**
     * Obtiene el cursor correspondiente a una determinada acción.
     */
    private Cursor obtenerCursor(int accion) {
        if (accion == DnDConstants.ACTION_MOVE) {
            return DragSource.DefaultMoveDrop;
        } else if (accion == DnDConstants.ACTION_COPY) {
            return DragSource.DefaultCopyDrop;
        } else {
            return null;//Cursor.getDefaultCursor();
        }
    }
    /**
     * Se invoca cuando se reconoce un gesto de arrastrar
     */     
    @Override
    public void dragGestureRecognized(DragGestureEvent evento) {
        Point posicion = evento.getDragOrigin();
        
        if (hayAlgoQueArrastrar(posicion)) {
            // Paso 1.4
            // Crear un objeto que implemente java.awt.datatransfer.Transferable
            // y poner en él la información que queremos permitir que sea arrastrada.
            Transferable tranferible = new ObjetoTransferible(posicion);
            
            // Paso 1.5
            dragSource.startDrag(evento, obtenerCursor(evento.getDragAction()), tranferible, this);
        }
    }
    
     // Paso 1.6: Eventos del objeto origen del arrastre
    @Override
    public void dragEnter(DragSourceDragEvent evento) {
    }

    @Override
    public void dragOver(DragSourceDragEvent evento) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent evento) {
        evento.getDragSourceContext().setCursor(obtenerCursor(evento.getDropAction()));
    }

    @Override
    public void dragExit(DragSourceEvent evento) {
    }

    // Paso 1.7: Responder a un drop
    @Override
    public void dragDropEnd(DragSourceDropEvent evento) {
        if (evento.getDropSuccess()) {
            //traza("Origen: Objeto arrojado");
        } else {
            //traza("Origen: El objeto no fue arrojado");
        }
    }

    // Paso 2.4 Eventos del objeto destino
    @Override
    public void dragExit(DropTargetEvent evento) {
    }

    @Override
    public void dragEnter(DropTargetDragEvent evento) {
        evento.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
    }

    @Override
    public void dragOver(DropTargetDragEvent evento) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent evento) {
    }

    // Paso 2.5: Evento de soltar en el objeto destino
    @Override
    public void drop(DropTargetDropEvent evento) {

        boolean aceptado = false;        

        Transferable objetoTransferido = evento.getTransferable();
        //System.out.println("Destino: Se ha soltado el objeto " + objetoTransferido);

        DataFlavor saboresTransferidos[] = objetoTransferido.getTransferDataFlavors();

        try {
            for (int i = 0; i < saboresTransferidos.length; i++) {

                if (aceptarSabor(saboresTransferidos[i])) {
                    // Paso 2.5: Se aceptan la operación de arrojar
                    evento.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

                    // Paso 2.6: Se extrae el objeto transferido
                    Object dato = objetoTransferido.getTransferData(saboresTransferidos[i]);

                    if (evento.getDropAction() == DnDConstants.ACTION_COPY) {
                        eventoCopiar(dato, evento.getLocation());
                    } else if (evento.getDropAction() == DnDConstants.ACTION_MOVE) {
                        eventoMover(dato, evento.getLocation());
                    }
                    
                    // Paso 2.7: Completar la operación de soltar y notificar a la fuente
                    // arrastrada (ésta recibirá una notificación dragDropEnd())                    
                    evento.getDropTargetContext().dropComplete(true);
                    aceptado = true;
                    break;
                }
            }
            // Paso 2.5: Se rechazan los objetos que no han sido aceptados
            if (!aceptado) {
                evento.rejectDrop();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            evento.rejectDrop();
        } catch (UnsupportedFlavorException ufException) {
            ufException.printStackTrace();
            evento.rejectDrop();
        }
    }
    
    public boolean aceptarSabor(DataFlavor dataFlavor) {
        return dataFlavor.getHumanPresentableName().equals(nombreSabor);
    }

    /**
     * Indica si se puede iniciar una operación de arrastrado.
     */        
    public abstract boolean hayAlgoQueArrastrar(Point posicion);
    
    /**
     * Crea el dato transferible para ser enviado al destino.
     */
    public abstract Object crearDatoTransferible(Point posicion);
    
    public abstract void eventoCopiar(Object datoTransferido, Point posicion);

    public abstract void eventoMover(Object datoTransferido, Point posicion);
}
