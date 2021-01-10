package es.jbp.comun.utiles.swing.tabla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.reflexion.Reflexion;

/**
 * Filtro generico para las tablas genericas
 * @author jberjano
 */
public class FiltroGenerico<T> implements Filtro<T> {
    private String[] nombresAtributos;
    private T entidadReferencia;
    
    public FiltroGenerico(String[] nombresAtributos) {
        this.nombresAtributos = nombresAtributos;
    }
    
    public void setEntidadFiltro(T entidadFiltro) {
        this.entidadReferencia = entidadFiltro;
    }
    
    public List<T> filtrar(List<T> lista) {
        List<T> listaFiltrada = new ArrayList();
        
        for (T elemento : lista) {
            if (cumpleFiltro(elemento)) {
                listaFiltrada.add(elemento);
            } 
        }
        return listaFiltrada;
    }   

    protected boolean cumpleFiltro(T elemento) {
        for (String nombreAtributo : nombresAtributos)  {
            Object valorElemento = Reflexion.obtenerValorAtributo(elemento, nombreAtributo);
            Object valorReferencia = Reflexion.obtenerValorAtributo(entidadReferencia, nombreAtributo);
            if (!sonValoresCompatibles(valorElemento, valorReferencia)) {  
                return false;
            }
        }
        return true;
    }
    
    
    protected boolean sonValoresCompatibles(Object valorElemento, Object valorReferencia) {
        if (valorReferencia == null || valorReferencia.toString().isEmpty()) {
            return true;
        }
                
        if (valorElemento == null) {
            return false;
        }
        // TODO: Contemplar todos los tipos de datos
        
        if (valorElemento.equals(valorReferencia)) {
            return true;
        }
        
        if (valorElemento instanceof String) {
            return valorElemento.toString().toUpperCase().contains(valorReferencia.toString().toUpperCase());
        }
        
        return false;
    }


}
