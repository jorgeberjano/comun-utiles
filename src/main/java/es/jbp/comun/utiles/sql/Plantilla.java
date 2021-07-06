package es.jbp.comun.utiles.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.recursos.GestorRecursos;
import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;

/**
 * Plantilla para sentencias SQL. Sustituye los simbolos {$simbolo} por valor
 * que tiene en el formateadorSql si lo tiene, si no por el del parámetro.
 * Sustituye los parametros :parametro por su valor SQL. Sustituye los
 * parametros {:parametro} por su valor textual.
 *
 * @author Jorge Berjano
 */
public class Plantilla {
    
    private String texto;
    private final Map<String, Object> mapaParametros = new HashMap<String, Object>();
    private final Map<String, Object> mapaSimbolos = new HashMap<String, Object>();

    private FormateadorSql formateadorSql;

    private boolean soloSimbolos = false;

    public Plantilla(String texto, FormateadorSql formateadorSql) {
        this.texto = texto;
        this.formateadorSql = formateadorSql;
    }

    public Plantilla(URL url, FormateadorSql formateadorSql) throws IOException {
        this(url.openStream(), formateadorSql);
    }
    
    public Plantilla(InputStream inputStream, FormateadorSql formateadorSql) throws IOException {
        this(GestorRecursos.leerTexto(inputStream), formateadorSql);
    }

    public boolean isSoloSimbolos() {
        return soloSimbolos;
    }

    public void setSoloSimbolos(boolean soloSimbolos) {
        this.soloSimbolos = soloSimbolos;
    }

    public void definirSimbolo(String simbolo, Object valor) {
        mapaSimbolos.put(simbolo, valor);
    }

    public Object obtenerSimbolo(String simbolo) {
        return mapaSimbolos.get(simbolo);
    }

    public void definirParametro(String parametro, Object valor) {
        mapaParametros.put(parametro, valor);
    }

    public Object obtenerParametro(String parametro) {
        return mapaParametros.get(parametro);
    }

    private StringBuilder sustituirSimbolosSql(String sql) {
        StringBuilder resultado = new StringBuilder();

        Pattern pattern = Pattern.compile("\\{\\$\\w*\\}");
        Matcher matcher = pattern.matcher(sql);
        int ultimaPosicion = 0;
        while (matcher.find()) {
            String group = matcher.group();
            int desde = matcher.start();
            int hasta = matcher.end();
            if (desde > 0) {
                resultado.append(sql.substring(ultimaPosicion, desde));
            }
            String simbolo = group.substring(2, group.length() - 1);
            String valorSimbolo = getValorSimbolo(simbolo);
            resultado.append(valorSimbolo);

            ultimaPosicion = hasta;
        }
        resultado.append(sql.substring(ultimaPosicion, sql.length()));
        return resultado;
    }

    private String getValorSimbolo(String simbolo) {
        
        String valor = null;
        if (formateadorSql != null) {
            valor = formateadorSql.getValorSimbolo(simbolo);
        }
        if (valor == null) {
            valor = Conversion.toString(obtenerSimbolo(simbolo));
        }
        return valor == null ? "{$" + simbolo + "}" : valor;
    }

    public String getResultado() {

        StringBuilder builder = new StringBuilder(sustituirSimbolosSql(texto));

        if (soloSimbolos) {
            return builder.toString();
        }

        // Se ordenan los parametros de mayor a menor para que los parametros
        // que sean subcadenas de otros no sustituyan simbolos parcialmente
        List<String> listaParametros = new ArrayList();
        listaParametros.addAll(mapaParametros.keySet());
        Collections.sort(listaParametros, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });

        // Se sustituyen los parametros {:parametro} por el texto tal cual
        // Se sustituyen los parametros :parametro por su valor SQL formateado
        for (String parametro : listaParametros) {
            Object valor = obtenerParametro(parametro);
            builder = reemplazarSimbolo(builder, "\\{:" + parametro + "\\}", Conversion.toString(valor));
            builder = reemplazarSimbolo(builder, ":" + parametro, formatearValor(valor));
        }

        StringBuilder bufferResultado = new StringBuilder();
        // Se procesan las directivas #ifdef #else y #endif
        Stack<Boolean> pilaCondiciones = new Stack<Boolean>();
        boolean condicionActual = true;
        BufferedReader reader = new BufferedReader(new StringReader(builder.toString()));
        String line = null;
        boolean primeraLinea = true;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--")) {
                    continue;
                } else if (line.startsWith("#ifdef")) {
                    String parametro = line.substring(6).trim();
                    Object valor = obtenerParametro(parametro);
                    boolean condicion = valor != null;
                    pilaCondiciones.push(condicionActual);
                    condicionActual = condicion;
                    continue;
                } else if (line.startsWith("#if")) {
                    String parametro = line.substring(3).trim();
                    Object valor = obtenerParametro(parametro);
                    Boolean condicion = Conversion.toBoolean(valor);
                    pilaCondiciones.push(condicionActual);
                    condicionActual = Boolean.TRUE.equals(condicion);
                    continue;
                } else if (line.startsWith("#else")) {
                    condicionActual = !condicionActual;
                    continue;
                } else if (line.startsWith("#endif")) {
                    if (pilaCondiciones.isEmpty()) {
                        //throw new Exception("#endif sin #ifdef previo en plantilla");
                        // Error: #endif sin #ifdef previo
                        continue;
                    }
                    condicionActual = pilaCondiciones.pop();
                    continue;
                }
                if (condicionActual) {
                    if (!primeraLinea) {
                       bufferResultado.append("\n");
                    } else {
                        primeraLinea = false;
                    }
                    bufferResultado.append(line);
                }
            }
        } catch (IOException ex) {
            // Esta excepcion no puede ocurrir porque se lee de un String
        }

        return bufferResultado.toString();
    }

    private StringBuilder reemplazarSimbolo(StringBuilder builder, String parametro, String texto) {
        if (parametro == null) {
            return builder;
        }
        if (texto == null) {
            texto = "";
        }
        Pattern pattern = Pattern.compile(parametro);
        Matcher matcher = pattern.matcher(builder);
        String resultado = matcher.replaceAll(texto);
        return new StringBuilder(resultado);
    }

    /**
     * Convierte el valor a texto en formato SQL segun su tipo.
     */
    private String formatearValor(Object valor) {
        if (valor instanceof Collection) {
            String resultado = "";
            Iterator iterador = ((Collection) valor).iterator();
            while (iterador.hasNext()) {
                if (!resultado.isEmpty()) {
                    resultado += ", ";
                }
                resultado += formatearValor(iterador.next());
            }
            return resultado;
        } else if (valor instanceof TextoPlano) {
            return valor.toString();
        }
        
        if (formateadorSql != null) {
            return formateadorSql.formatear(valor);        
        }
        return Conversion.toString(valor);
    }
}
