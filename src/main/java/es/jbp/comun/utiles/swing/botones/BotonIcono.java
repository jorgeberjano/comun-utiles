package es.jbp.comun.utiles.swing.botones;

import es.jbp.comun.utiles.geometria.Direccion;
import es.jbp.comun.utiles.imagen.UtilidadesImagen;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Boton que presenta un icono y se desplaza cuando es pulsado.
 *
 * @author jberjano
 */
public class BotonIcono extends BotonEscalable {
    
    private final static int DELTA_PULSACION = 3;

    public BotonIcono() {
        super();
        super.setFocusable(false);
        super.setBorderPainted(false);
        super.setOpaque(false);
        super.setContentAreaFilled(false);
    }

    @Override
    public void setIcon(Icon icon) {
        
        if (!(icon instanceof ImageIcon) || getEscala() == 0) {
            super.setIcon(icon);
            return;
        }
        ImageIcon imagenIcono = (ImageIcon) icon;        
        Image imagen = imagenIcono.getImage();
        
        int anchoLienzo = (int) (imagen.getWidth(this) * escala);
        int altoLienzo = (int) (imagen.getHeight(this) * escala);
        int anchoImagen = (int) (anchoLienzo  - DELTA_PULSACION * escala);
        int altoImagen = (int) (altoLienzo - DELTA_PULSACION * escala);
        
        Image imagenEscalada = escalarIcono(anchoImagen, altoImagen, imagenIcono).getImage();
        //Image imagenEscalada = escalarIcono2(anchoImagen, altoImagen, imagenIcono.getImage());
        //Image imagenEscalada = imagen.getScaledInstance(anchoImagen, altoImagen, java.awt.Image.SCALE_SMOOTH);
        //Image imagenEscalada = iconoEscalado.getImage();
        Image imagenNormal = UtilidadesImagen.escalarLienzo(imagenEscalada,
                anchoImagen, altoImagen, anchoLienzo, altoLienzo, Direccion.NOROESTE);
        
        Image imagenPulsada = UtilidadesImagen.escalarLienzo(imagenEscalada,
                anchoImagen, altoImagen, anchoLienzo, altoLienzo, Direccion.SURESTE);
        
        super.setIcon(new ImageIcon(imagenNormal));
        super.setPressedIcon(new ImageIcon(imagenPulsada));
    }

    private ImageIcon escalarIcono(int anchoImagen, int altoImagen, ImageIcon imagenIcono) {
        Image imagenEscalada = imagenIcono.getImage().getScaledInstance(anchoImagen, altoImagen, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }
    
//    private Image escalarIcono2(int anchoImagen, int altoImagen, Image imagen) {
//        Image imagenEscalada = imagen.getScaledInstance(anchoImagen, altoImagen, java.awt.Image.SCALE_SMOOTH);
//        //return new ImageIcon(imagenEscalada);
//        return imagenEscalada;
//    }
}
