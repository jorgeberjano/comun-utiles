package es.jbp.comun.utiles.swing.modelos;

import java.util.List;
import javax.swing.ComboBoxModel;

/**
 * Modelo de lista combo que hace de adaptador de los objetos que cumplen el
 * contrato lista.
 *
 * @author Jorge Berjano
 */
public class ModeloListaCombo extends ModeloLista implements ComboBoxModel {
    private Object seleccionado;
    
    public ModeloListaCombo() {
    }
    
    /**
     * Contructor.
     *
     * param listaObjetos La lista de objetos que componen el modelo.
     */
    public ModeloListaCombo(List listaObjetos) {
        super(listaObjetos);
    }


    public void setSelectedItem(Object anItem) {
        seleccionado = anItem;
        fireContentsChanged(this, -1, -1);        
    }

    public Object getSelectedItem() {
        return seleccionado;
    }    
}
