package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para los objetos encargados de contruir entidades a parir de
 * resultados de consultas.
 * @author jberjano
 */
public abstract class ConstructorEntidad<T> {
    private Integer numeroMaximoElementos = 10000; 
    protected FormateadorSql formateadorSql;

    public FormateadorSql getFormateadorSql() {
        return formateadorSql;
    }

    public void setFormateadorSql(FormateadorSql formateadorSql) {
        this.formateadorSql = formateadorSql;
    }
    
    public Integer getNumeroMaximoElementos() {
        return numeroMaximoElementos;
    }

    public void setNumeroMaximoElementos(Integer numeroMaximoElementos) {
        this.numeroMaximoElementos = numeroMaximoElementos;
    }

    protected abstract T construirEntidad(AdaptadorResultSet rs) throws Exception;

    public T construirEntidadOpcional(AdaptadorResultSet rs) {
        try {
            return construirEntidad(rs);
        } catch (Exception e) {
            return null;
        }
    }

    public T construirEntidad(AdaptadorResultSet rs, int numeroOcurrencia) throws Exception {
        rs.setNumeroOcurrencia(numeroOcurrencia);
        T entidad = construirEntidad(rs);
        rs.setNumeroOcurrencia(1);
        return entidad;
    }

    public T construirEntidadOpcional(AdaptadorResultSet rs, int numeroOcurrencia) {
        rs.setNumeroOcurrencia(numeroOcurrencia);
        T entidad = null;
        try {
            entidad = construirEntidad(rs);
        } catch (Exception e) {
        }
        rs.setNumeroOcurrencia(1);
        return entidad;
    }

    public T obtenerEntidad(ResultSet rs) throws Exception {
        if (!rs.next()) {
            return null;
        }
        return construirEntidad(new AdaptadorResultSet(rs, formateadorSql));
    }
    
    private T obtenerEntidad(AdaptadorResultSet rs) throws Exception {
        if (!rs.next()) {
            return null;
        }
        return construirEntidad(rs);
    }

    public List<T> obtenerListaEntidades(ResultSet rs) throws Exception {
        List<T> resultado = new ArrayList();
        T entidad;
        AdaptadorResultSet resultset = new AdaptadorResultSet(rs, formateadorSql);
        do {
            entidad = obtenerEntidad(resultset);
            if (entidad != null) {
                resultado.add(entidad);
            }
            if (numeroMaximoElementos != null && resultado.size() > numeroMaximoElementos) {
                throw new Exception ("El número de elementos resultantes de la consulta excede el máximo permitido");
            }
        } while (entidad != null);
        return resultado;
    }

    public List<T> obtenerPaginaEntidades(ResultSet rs, int indicePrimerElemento, int numeroElementos) throws Exception {

        List<T> resultado = new ArrayList(numeroElementos);
        T entidad;
        if (indicePrimerElemento > 0) {
            rs.absolute(indicePrimerElemento);
        }
        int i = 0;
        do {
            entidad = obtenerEntidad(rs);
            if (entidad != null) {
                resultado.add(entidad);
            }
            if (++i >= numeroElementos) {
                break;
            }
            if (numeroMaximoElementos != null && i > numeroMaximoElementos) {
                throw new Exception ("El número de elementos resultantes de la consulta excede el máximo permitido");
            }
        } while (entidad != null);
        return resultado;
    }
}
