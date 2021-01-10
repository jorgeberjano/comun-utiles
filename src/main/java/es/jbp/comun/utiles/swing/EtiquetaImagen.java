package es.jbp.comun.utiles.swing;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author jberjano
 */
public class EtiquetaImagen extends JLabel {

    private Image imagen;
    private boolean mantenerProporcion = true;

    public EtiquetaImagen() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                escalarImagen();
                super.componentResized(e);
            }
        });
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
        escalarImagen();
    }

    public boolean isMantenerProporcion() {
        return mantenerProporcion;
    }

    public void setMantenerProporcion(boolean mantenerProporcion) {
        this.mantenerProporcion = mantenerProporcion;
    }

    @Override
    public void setIcon(Icon icono) {
        if (icono instanceof ImageIcon) {
            imagen = ((ImageIcon) icono).getImage();
            escalarImagen();

        } else {
            super.setIcon(icono);
        }
    }

    private void escalarImagen() {
        
        if (imagen == null) {
            super.setIcon(null);
            super.revalidate();
            return;
        }        
        int anchoEtiqueta = getWidth();
        int altoEtiqueta = getHeight();
        if (anchoEtiqueta * altoEtiqueta == 0) {
            return;
        }
        double proporcionEtiqueta = anchoEtiqueta / (double) altoEtiqueta;

        int anchoImagen = imagen.getWidth(null);
        int altoImagen = imagen.getHeight(null);
        if (anchoImagen * altoImagen == 0) {
            return;
        }
        double proporcionImagen = anchoImagen / (double) altoImagen;

        if (mantenerProporcion) {
            if (proporcionEtiqueta > proporcionImagen) {
                // La imagen se ajusta al alto de la etiqueta
                anchoEtiqueta = (int) (altoEtiqueta * proporcionImagen);
            } else {
                // La imagen se ajusta al ancho de la etiqueta
                altoEtiqueta = (int) (anchoEtiqueta / proporcionImagen);
            }
        }

        Image imagenEscalada = imagen.getScaledInstance(anchoEtiqueta, altoEtiqueta, Image.SCALE_SMOOTH);
        super.setIcon(new ImageIcon(imagenEscalada));
    }

}
