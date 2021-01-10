package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.conversion.Conversion;
import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaAbstracta;
import es.jbp.comun.utiles.tiempo.FechaHora;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;
import java.sql.ResultSet;
import java.sql.RowId;
import java.util.Map;

/**
 * Clase base para los ejecutores de sentecias de guardado de entidades (insert
 * y update)
 *
 * @author jberjano
 */
public abstract class EjecutorSentenciaGuardado extends Ejecutor {

    protected boolean valoresComoTexto = false;
    protected boolean generarSecuenciasPreviamente = false;
    protected final GestorConexiones gestorConexiones;
    protected String tabla;
    protected final List<Campo> listaCampos = new ArrayList<Campo>();
    protected final List<Campo> listaPKs = new ArrayList<Campo>();
    private int indice = 1;
    protected Map valoresRecuperados;

    protected static class Campo {

        String nombre;
        Object valor;

        Campo(String nombre, Object valor) {
            this.nombre = nombre;
            this.valor = valor;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Campo) || nombre == null) {
                return false;
            }
            return nombre.equals(((Campo) o).nombre);
        }
    }

    public EjecutorSentenciaGuardado(GestorConexiones gestorConexiones) {
        this.gestorConexiones = gestorConexiones;
    }

    public boolean isValoresComoTexto() {
        return valoresComoTexto;
    }

    public void setValoresComoTexto(boolean valoresComoTexto) {
        this.valoresComoTexto = valoresComoTexto;
    }

    public abstract boolean ejecutar() throws Exception;

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public void limpiar() {
        indice = 1;
        listaCampos.clear();
        listaPKs.clear();
    }

    public boolean contieneCampo(String nombre) {
        return listaCampos.stream().filter(campo -> campo.nombre.equals(nombre)).findFirst().isPresent();
    }

    public boolean contienePk(String nombre) {
        return listaPKs.stream().filter(campo -> campo.nombre.equals(nombre)).findFirst().isPresent();
    }

    public void agregarCampo(String nombre, Object valor) {
        if (contieneCampo(nombre)) {
            return;
        }
        listaCampos.add(new Campo(nombre, valor));
    }

    public void agregarPk(String nombre, Object valor) {
        if (contienePk(nombre)) {
            return;
        }
        listaPKs.add(new Campo(nombre, valor));
    }

    protected void asignarValor(PreparedStatement statement, Object valor) throws Exception {

        valor = gestorConexiones.getFormateadorSql().convertirAlPersistir(valor);

        if (valor == null || valor instanceof Secuencia) {
            return;
        }
        if (valor instanceof FechaAbstracta && ((FechaAbstracta) valor).esPresente()) {
            return;
        }

        if (valor instanceof Boolean) {
            statement.setBoolean(indice, (Boolean) valor);
        } else if (valor instanceof Integer) {
            statement.setInt(indice, (Integer) valor);
        } else if (valor instanceof Long) {
            statement.setLong(indice, (Long) valor);
        } else if (valor instanceof Float) {
            statement.setFloat(indice, (Float) valor);
        } else if (valor instanceof Double) {
            statement.setDouble(indice, (Double) valor);
        } else if (valor instanceof Fecha) {
            statement.setDate(indice, ((FechaAbstracta) valor).toSqlDate());
        } else if (valor instanceof FechaHora || valor instanceof FechaHoraMs) {
            statement.setTimestamp(indice, ((FechaAbstracta) valor).toTimestamp());
        } else if (valor instanceof byte[]) {
            byte[] array = (byte[]) valor;
            statement.setBinaryStream(indice, new ByteArrayInputStream(array), array.length);
        } else if (valor instanceof Enum) {
            statement.setInt(indice, ((Enum) valor).ordinal());
        } else {
            statement.setString(indice, valor.toString());
        }
        indice++;
    }

    protected String getSqlValor(Campo campo) throws Exception {
        if (campo.valor == null) {
            return "null";
        }

        if (campo.valor instanceof ValorSql) {
            return ((ValorSql) campo.valor).getValorSql(gestorConexiones.getFormateadorSql());
        }
        // TODO: implementar las fechas presentes como ValorSql
        if (campo.valor instanceof FechaAbstracta) {
            FechaAbstracta fechaHora = (FechaAbstracta) campo.valor;

            if (fechaHora.esPresente()) {
                if (campo.valor instanceof Fecha) {
                    return gestorConexiones.getFormateadorSql().getFechaActual();
                } else if (campo.valor instanceof FechaHoraMs) {
                    return gestorConexiones.getFormateadorSql().getFechaHoraMsActual();
                } else {
                    return gestorConexiones.getFormateadorSql().getFechaHoraActual();
                }
            }
        }

        // TODO: Se cambia la implementacion !!!
        if (campo.valor instanceof Secuencia) {
            if (generarSecuenciasPreviamente) {
                campo.valor = obtenerValorSecuencia((Secuencia) campo.valor, campo);
            } else {
                Secuencia secuencia = (Secuencia) campo.valor;
                secuencia.setFormateadorSql(gestorConexiones.getFormateadorSql());
                String sql = "(" + secuencia.getSql(tabla, campo.nombre) + ")";
                return sql;
            }
        }

        if (valoresComoTexto) {
            return gestorConexiones.getFormateadorSql().formatear(campo.valor);
        }
        return "?";
    }

    protected Long obtenerValorSecuencia(Secuencia secuencia, Campo campo) throws Exception {
        secuencia.setFormateadorSql(gestorConexiones.getFormateadorSql());
        String sql = secuencia.getSelectSql(tabla, campo.nombre);
        EjecutorSentenciaSelect ejecutorSelect = new EjecutorSentenciaSelect(gestorConexiones);
        Long id = ejecutorSelect.obtenerEntidad(sql, new ConstructorLong());
        secuencia.setValorGenerado(id);
        return id;
    }

    protected Iterable<Campo> getListaTodosCampos() {
        List<Campo> listaTodosCampos = new ArrayList<Campo>();

        for (Campo campo : listaPKs) {
            if (!listaCampos.contains(campo)) {
                listaTodosCampos.add(campo);
            }
        }
        listaTodosCampos.addAll(listaCampos);

        return listaTodosCampos;
    }

    public Map getValoresRecuperados() {
        return valoresRecuperados;
    }

    protected void recuperarValores(PreparedStatement statement) {

        if (generarSecuenciasPreviamente) {
            recuperarValorSecuencias();
            return;
        }

        try {
            ResultSet rs = statement.getGeneratedKeys();
            if (!rs.next()) {
                return;
            }
            Object id = rs.getObject(1);
            if (id instanceof Integer) {
                for (Campo campo : listaPKs) {
                    if (campo.valor instanceof Secuencia) {
                        Secuencia secuencia = (Secuencia) campo.valor;
                        secuencia.setValorGenerado(Conversion.toLong(id));
                        break;
                    }
                }
            } else if (id instanceof RowId) {
                RowId rowId = (RowId) id;
                String strRowId = new String(rowId.getBytes());
                EjecutorSentenciaSelect ejecutor = new EjecutorSentenciaSelect(gestorConexiones);
                ConstructorGenerico constructor = new ConstructorGenerico();
                String sql = "SELECT * FROM " + tabla + " WHERE ROWID = '" + strRowId + "'";
                valoresRecuperados = ejecutor.obtenerEntidad(sql, constructor);
            }
        } catch (Exception ex) {
            // Si la tabla no tiene clave primaria ocurrirá una excepción que se ignora
        }
    }

    private void recuperarValorSecuencias() {
        for (Campo campo : listaPKs) {
            if (campo.valor instanceof Secuencia) {
                Secuencia secuencia = (Secuencia) campo.valor;
                Object valor = valoresRecuperados.get(campo.nombre);
                secuencia.setValorGenerado(Conversion.toLong(valor));
            }
        }
    }

}
