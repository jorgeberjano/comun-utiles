package es.jbp.comun.utiles.swing.modelos;

import java.util.List;

/**
 * Contrato para los filtros de listas.
 * @author Jorge Berjano
 */
public interface FiltroLista<T> {

    List<T> filtrar(List<T> lista);    
}
