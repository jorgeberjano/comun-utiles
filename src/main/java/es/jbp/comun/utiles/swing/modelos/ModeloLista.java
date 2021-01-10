package es.jbp.comun.utiles.swing.modelos;

import java.util.List;
import javax.swing.AbstractListModel;

/**
 * Modelo de lista que hace de adaptador de los objetos que cumplen el
 * contrato lista.
 *
 * @author Jorge Berjano
 */
public class ModeloLista extends AbstractListModel {

    protected List listaObjetos;
    protected List listaObjetosFiltrada;
    protected Formateador formateador;

    public ModeloLista() {
    }
    
    /**
     * Constructor
     * @param listaObjetos Lista de los objetos a mostrar.
     */
    public ModeloLista(List listaObjetos) {
        this.listaObjetos = listaObjetos;
        this.listaObjetosFiltrada = listaObjetos;
    }

    /**
     * Constructor
     * @param listaObjetos Lista de los objetos a mostrar.
     * @param formateador Se encarga de formatear la cadena visible.
     */
    public ModeloLista(List listaObjetos, Formateador formateador) {
        this.listaObjetos = listaObjetos;
        this.listaObjetosFiltrada = listaObjetos;
        this.formateador = formateador;
    }

    @Override
    public int getSize() {
        return listaObjetosFiltrada != null ? listaObjetosFiltrada.size() : 0;
    }

    @Override
    public Object getElementAt(int index) {
        if (index > listaObjetosFiltrada.size() - 1)
            return null;
        Object valor = listaObjetosFiltrada.get(index);
        if (formateador != null) {
            return formateador.formatear(valor);
        } else {
            return valor;
        }
    }

    public void setListaObjetos(List listaObjetos) {
        this.listaObjetos = listaObjetos;
        this.listaObjetosFiltrada = listaObjetos;
        actualizar();
    }

    public void setFormateador(Formateador formateador) {
        this.formateador = formateador;
    }

    /**
     * Fuerza actualizar toda la lista.
     */
    public void actualizar() {
        fireContentsChanged(this, 0, listaObjetosFiltrada.size() - 1);
    }

    public void aplicarFiltro(FiltroLista filtro) {
        if (listaObjetos == null) {
            listaObjetosFiltrada = null;
        } else {
            listaObjetosFiltrada = filtro.filtrar(listaObjetos);
        }
        actualizar();
    }
}
