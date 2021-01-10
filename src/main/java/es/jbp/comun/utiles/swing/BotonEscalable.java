package es.jbp.comun.utiles.swing.botones;

import javax.swing.JButton;

/**
 * Clase base para botones escalables
 * @author jberjano
 */
public class BotonEscalable extends JButton {
    protected static double escala = 1;

    public static double getEscala() {
        return escala > 0 ? escala : 1;
    }

    public static void setEscala(double escala) {
        BotonEscalable.escala = escala;
    }
    
}
