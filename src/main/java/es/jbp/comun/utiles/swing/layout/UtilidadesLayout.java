package es.jbp.comun.utiles.swing.layout;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;
import javax.swing.JPanel;

/**
 * Utilidades para utilizar con layouts
 *
 * @author jberjano
 */
public class UtilidadesLayout {

    public static void distribuirProporcionalmente(JPanel panel, int ancho, int alto, boolean vertical, double... proporciones) {
        int n = proporciones.length;
        double suma = 0;
        for (int i = 0; i < n; i++) {
            suma += proporciones[i];
        }
        int i = 0;
        for (Component comp : panel.getComponents()) {
            if (i >= n) {
                break;
            }
            double proporcion = proporciones[i] / suma;
            int nuevoAncho = vertical ? ancho : (int) (ancho * proporcion);
            int nuevoAlto = vertical ? (int) (alto * proporcion) : alto;
            asignarTamanoFijo(comp, nuevoAncho, nuevoAlto);
            comp.revalidate();
            comp.repaint();
            i++;
        }
        panel.revalidate();
        panel.repaint();
    }

    public static void asignarTamanoFijo(Component comp, int nuevoAncho, int nuevoAlto) {
        Dimension dimension = new Dimension(nuevoAncho, nuevoAlto);
        comp.setPreferredSize(dimension);
        comp.setMinimumSize(dimension);
        comp.setMaximumSize(dimension);
    }
}
