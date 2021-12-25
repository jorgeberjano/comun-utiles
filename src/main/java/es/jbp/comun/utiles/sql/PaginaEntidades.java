package es.jbp.comun.utiles.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagina de entidades que contiene la lista de entidades y el total de entidades
 * disponibles
 * @author jberjano
 */
public class PaginaEntidades<T> {

    private List<T> listaEntidades;
    private long numeroTotalEntidades;

    public List<T> getListaEntidades() {
        return listaEntidades;
    }

    public void setListaEntidades(List<T> listaEntidades) {
        this.listaEntidades = listaEntidades;
    }

    public long getNumeroTotalEntidades() {
        return numeroTotalEntidades;
    }

    public void setNumeroTotalEntidades(long numeroTotalEntidades) {
        this.numeroTotalEntidades = numeroTotalEntidades;
    }
    
    public void agregar(T entidad) {
        
        if (listaEntidades == null) {
            listaEntidades = new ArrayList();
        }
        listaEntidades.add(entidad);
    }
}
