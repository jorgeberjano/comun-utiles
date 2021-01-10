package es.jbp.comun.utiles.recursos;

import es.jbp.comun.utiles.conversion.Propiedades;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import es.jbp.comun.utiles.depuracion.GestorLog;
import java.io.File;
import java.io.FileInputStream;

/**
 * Singleton que gestiona los recursos de la aplicación.
 * @author jberjano
 */
public class GestorRecursos {

    private static GestorRecursos instancia;
    private Map<String, String> mapaRecursosTexto = new HashMap();
    private Map<String, ImageIcon> mapaRecursosImagen = new HashMap();

    private GestorRecursos() {
    }

    public static synchronized GestorRecursos getInstancia() {
        if (instancia == null) {
            instancia = new GestorRecursos();
        }
        return instancia;
    }

    public ImageIcon getRecursoImagen(String nombreRecurso) {
        
        if (mapaRecursosImagen.containsKey(nombreRecurso)) {
            return mapaRecursosImagen.get(nombreRecurso);
        }
        URL url = getClass().getResource(nombreRecurso);
        if (url == null) {
            GestorLog.error("No se encuentra el recurso " + nombreRecurso, null);
            return null;
        }
        ImageIcon imagen = new ImageIcon(url);
        mapaRecursosImagen.put(nombreRecurso, imagen);
        return imagen;
    }

    public String getRecursoTexto(String nombreRecurso) throws IOException {

        if (mapaRecursosTexto.containsKey(nombreRecurso)) {
            return mapaRecursosTexto.get(nombreRecurso);
        }
        URL url = getClass().getResource(nombreRecurso);
        if (url == null) {
            GestorLog.error("No se encuentra el recurso " + nombreRecurso, null);
            return null;
        }
        String texto = leerTexto(url);
        mapaRecursosTexto.put(nombreRecurso, texto);
        return texto;
    }

    public static String leerTexto(URL url) throws IOException {
        return leerTexto(url.openStream());
    }

    public static String leerTexto(InputStream inputStream) throws IOException {
        StringBuilder buider = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buider.append(line);
                buider.append("\n");
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return buider.toString();
    }

    public InputStream getStreamRecurso(String nombreRecurso) {
        try {
            URL url = getClass().getResource(nombreRecurso);
            if (url == null) {
                GestorLog.error("No se encuentra el recurso " + nombreRecurso, null);
                return null;
            }
            return url.openStream();      
        } catch (Exception ex) {
            GestorLog.error("No se ha podido cargar el recurso " + nombreRecurso, ex);
            return null;
        }
        
    }
    
    public Propiedades getRecursoPropiedades(String nombreRecurso) {
        try {
            InputStream entrada = GestorRecursos.getInstancia().getStreamRecurso(nombreRecurso);
            return getPropiedades(entrada);
        } catch (Exception ex) {
            GestorLog.error("No se ha podido cargar el recurso de propiedades " + nombreRecurso, ex);
            return new Propiedades();
        }
    }   
    
    public Propiedades getPropiedades(InputStream entrada) {
        try {
            Propiedades propiedades = new Propiedades();
            if (entrada != null) {
                propiedades.cargar(entrada);
                entrada.close();
            }
            return propiedades;
        } catch (Exception ex) {
            GestorLog.error("No se ha podido cargar el archivo de propiedades", ex);
            return new Propiedades();
        }    
    }
    
    public Propiedades getArchivoPropiedades(String nombreArchivo) {                
        return getPropiedades(getStreamArchivo(nombreArchivo));            
    }
    
    public FileInputStream getStreamArchivo(String nombreArchivo) {
        String pathConfig = System.getProperty("user.dir");
        try {
            return new FileInputStream(new File(pathConfig + nombreArchivo));            
        } catch (IOException ex) {
            GestorLog.error("No se ha podido cargar el archivo " + nombreArchivo, ex);
            return null;
        }   
    }
}
