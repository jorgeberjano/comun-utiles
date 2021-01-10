package es.jbp.comun.utiles.swing.botones;

import es.jbp.comun.utiles.geometria.Direccion;
import es.jbp.comun.utiles.imagen.UtilidadesImagen;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Boton que se muestra con una imagen
 *
 * @author jberjano
 */
public class BotonImagen extends BotonEscalable {

    private final static int DELTA_PULSACION = 3;
    

    /**
     * Pinta el componente.
     */
    @Override
    public void paint(Graphics g) {

        if (getIcon() instanceof ImageIcon) {
            Image imagen = ((ImageIcon) getIcon()).getImage();

            int anchoLienzo = getWidth();
            int altoLienzo = getHeight();
           
            if (isOpaque()) {
                g.setColor(getBackground());
                g.fillRect(0, 0, anchoLienzo, altoLienzo);
            }

            if (imagen != null) {
                int delta = (int) (DELTA_PULSACION * escala);               

                if (getModel().isPressed()) {
                    g.translate(delta, delta);
                }
                UtilidadesImagen.pintar(g, imagen, anchoLienzo, altoLienzo, Direccion.CENTRO);
            }
        }
    }
}
