package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import java.sql.ResultSet;
import java.sql.SQLException;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaHora;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;
import java.math.BigDecimal;

/**
 * Adaptador para un resultset con funciones para acceder a los valores de los
 * campos pudiendo devolver valores nulos.
 *
 * @author jberjano
 */
public class AdaptadorResultSet {

    private final ResultSet resultSet;
    private final MetadatosConsulta metadatos;
    private int numeroOcurrencia = 1;
    private final FormateadorSql formateadorSql;

    public AdaptadorResultSet(ResultSet resultSet, FormateadorSql formteadorSql) {
        this.resultSet = resultSet;
        metadatos = new MetadatosConsulta(resultSet);
        this.formateadorSql = formteadorSql;
    }

    /**
     * Asigna el numero de ocurrencia de los campos cuyos valores se van a
     * recuperar. Sirve para poder recuperar valores de campos que tienen el
     * mismo nombre debido a que hay una tabla relacionada doblemente con otra.
     *
     * @param numeroOcurrencia
     */
    public void setNumeroOcurrencia(int numeroOcurrencia) {
        this.numeroOcurrencia = numeroOcurrencia;
    }

    public Object getObject(String campo) throws SQLException {
        Object valor;
        if (numeroOcurrencia > 1) {
            int indice = metadatos.getIndice(campo, numeroOcurrencia);
            valor = resultSet.getObject(indice);
        } else {
            valor = resultSet.getObject(campo);
        }
        return resultSet.wasNull() ? null : valor;
    }

    public Object getObjectOpcional(String campo) {
        try {
            return getObject(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getInteger(String campo) throws SQLException {
        Object valor = getObject(campo);
        return Conversion.toInteger(valor);     
    }

    public Integer getIntegerOpcional(String campo) {
        try {
            return getInteger(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getLong(String campo) throws SQLException {
        Object valor = getObject(campo);
        return Conversion.toLong(valor);     
    }

    public Long getLongOpcional(String campo) {
        try {
            return getLong(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public Float getFloat(String campo) throws SQLException {
        Object valor = getObject(campo);
        return Conversion.toFloat(valor);     
    }

    public Float getFloatOpcional(String campo) {
        try {
            return getFloat(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public Double getDouble(String campo) throws SQLException {
        Object valor = getObject(campo);
        return Conversion.toDouble(valor);     
    }

    public Double getDoubleOpcional(String campo) {
        try {
            return getDouble(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public BigDecimal getBigDecimal(String campo) throws SQLException {
        Object valor = getObject(campo);
        return Conversion.toBigDecimal(valor);
    }

    public BigDecimal getBigDecimalOpcional(String campo) {
        try {
            return getBigDecimal(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public String getString(String campo) throws SQLException {
        Object valor = getObject(campo);
        return Conversion.toString(valor);     
    }

    public String getStringOpcional(String campo) {
        try {
            return getString(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public Fecha getFecha(String campo) throws SQLException {
        Object valor = getObject(campo);      
        if (formateadorSql != null) {
            return formateadorSql.toFecha(valor);
        } else {
            return Conversion.toFecha(valor);
        }        
    }

    public Fecha getFechaOpcional(String campo) {
        try {
            return getFecha(campo);
        } catch (Exception e) {
            return null;
        }
    }
       
    public FechaHora getFechaHora(String campo) throws SQLException {
        Object valor = getObject(campo);        
        if (resultSet.wasNull()) {
            return null;
        }
        if (formateadorSql != null) {
            return formateadorSql.toFechaHora(valor);
        } else {
            return Conversion.toFechaHora(valor);
        }
        
    }

    public FechaHora getFechaHoraOpcional(String campo) {
        try {
            return getFechaHora(campo);
        } catch (Exception e) {
            return null;
        }
    }
    
    public FechaHoraMs getFechaHoraMs(String campo) throws SQLException {
        Object valor = getObject(campo);
        if (resultSet.wasNull()) {
            return null;
        }
        if (formateadorSql != null) {
            return formateadorSql.toFechaHoraMs(valor);
        } else {
            return Conversion.toFechaHoraMs(valor);
        }     
    }
    
    public FechaHoraMs getFechaHoraMsOpcional(String campo) {
        try {
            return getFechaHoraMs(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean getBoolean(String campo) throws SQLException {
        Object valor = getObject(campo);
        return Conversion.toBoolean(valor);
    }

    public Boolean getBooleanOpcional(String campo) {
        try {
            return getBoolean(campo);
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] getBytes(String campo) throws Exception {
        Object valor = getObject(campo);
        return Conversion.toBytes(valor);     
    }

    public byte[] getBytesOpcional(String campo) {
        try {
            return getBytes(campo);
        } catch (Exception e) {
            return null;
        }
    }
    
//    public Blob getBlob(String campo) throws SQLException {
//        Blob valor;
//
//        if (numeroOcurrencia > 1) {
//            int indice = metadatos.getIndice(campo, numeroOcurrencia);
//            valor = resultSet.getBlob(indice);
//        } else {
//            valor = resultSet.getBlob(campo);
//        }
//        return resultSet.wasNull() ? null : valor;
//    }
//
//    public Blob getBlobOpcional(String campo) {
//        try {
//            return getBlob(campo);
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public Object get(final String nombreCampo) throws Exception {
        return get(new Campo() {
            @Override
            public String getNombre() {
                return nombreCampo;
            }

            @Override
            public TipoDato getTipoDato() {
                try {
                    return metadatos.getCampo(nombreCampo).getTipoDato();
                } catch (SQLException ex) {
                    return null;
                }
            }

        });
    }

    public Object get(Campo campo) throws Exception {
        if (campo == null || campo.getNombre() == null || campo.getTipoDato() == null) {
            return null;
        }
        
        String nombre = campo.getNombre();
        
        switch (campo.getTipoDato()) {
            case CADENA:
                return getString(nombre);
            case ENTERO:
                return getLong(nombre);
            case REAL:
                return getDouble(nombre);
            case BOOLEANO:
                return getBoolean(nombre);
            case FECHA:
                return getFecha(nombre);
            case FECHA_HORA:
                return getFechaHora(nombre);
            case BYTES:
                return getBytes(nombre);
            default:
                return null;
        }
    }

    public ResultSet getResulset() {
        return resultSet;
    }

    public MetadatosConsulta getMetadatos() {
        return metadatos;
    }

    public boolean next() throws SQLException {
        return resultSet.next();
    }

}
