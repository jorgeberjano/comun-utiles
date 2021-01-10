package es.jbp.comun.utiles.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import es.jbp.comun.utiles.depuracion.GestorLog;
import es.jbp.comun.utiles.geometria.Direccion;
import es.jbp.comun.utiles.imagen.UtilidadesImagen;

/**
 * Componente para visualizar un archivo de imagen.
 * 
 * @author Jorge Berjano
 */
public final class ComponenteImagen extends JComponent {

    private Image imagen;
    private String ruta;
    private Direccion direccion = Direccion.CENTRO;
    private Dimension preferredSize = new Dimension(0, 0);

    public ComponenteImagen() {
    }

    public ComponenteImagen(String ruta) {
        this();
        setRuta(ruta);
    }

    public ComponenteImagen(Image imagen) {
        this();
        this.imagen = imagen;
    }

    public ComponenteImagen(InputStream is) {
        this();
        setInputStream(is);
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
        if (ruta == null || ruta.isEmpty()) {
            setImagen(null);
            return;
        }

        try {
            setImagen(UtilidadesImagen.cargarImagen(ruta, getWidth(), getHeight()));
        } catch (Exception ex) {
            GestorLog.error("No se ha podido cargar la imagen " + ruta, ex);
        }
    }

    public void setInputStream(InputStream is) {
        try {
            setImagen(UtilidadesImagen.cargarImagen(is));
        } catch (Exception ex) {
            Logger.getLogger(ComponenteImagen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
        if (preferredSize.getHeight() == 0 && preferredSize.getHeight() == 0) {
            preferredSize = new Dimension(imagen.getWidth(this), imagen.getHeight(this));
        }
        validate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
    }

    /**
     * Pinta este componente.
     */
    @Override
    public void paint(Graphics g) {

        int ancho = getWidth();
        int alto = getHeight();
        
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, ancho, alto);
        }
        
        if (imagen != null) {
            UtilidadesImagen.pintar(g, imagen, ancho, alto, direccion);
        }
    }
}
