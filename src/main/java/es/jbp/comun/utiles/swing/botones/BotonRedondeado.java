package es.jbp.comun.utiles.swing.botones;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

/**
 * Boton con aspecto redondeado.
 *
 * @author Jorge Berjano
 */
public class BotonRedondeado extends BotonIcono {

    private static final int DELTA_PULSACION = 3;
    private static final int DELTA_SOMBRA = 4;

    public BotonRedondeado() {
        super();
        super.setFocusable(false);
        super.setBorderPainted(false);
        super.setOpaque(false);
        super.setContentAreaFilled(false);
    }

    protected Rectangle base;
    protected Shape shape;

    private void pintar(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int deltaPulsacion = getDeltaPulsacion();
        int deltaSombra = getDeltaSombra();

        if (getModel().isArmed()) {
            g2.translate(deltaPulsacion, deltaPulsacion);
        }

        // Sombra
        g2.setColor(new Color(0, 0, 0, 127));
        g2.translate(deltaSombra, deltaSombra);
        g2.fill(shape);

        // Boton
        if (isEnabled()) {
            g2.setColor(getBackground());
        } else {
            g2.setColor(Color.lightGray);
        }
        g2.translate(-deltaSombra, -deltaSombra);
        g2.fill(shape);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int deltaPulsacion = getDeltaPulsacion();
        int deltaSombra = getDeltaSombra();
        
        Dimension s = getSize();
        base = getBounds();
        double radio = Math.min(s.height, s.width) / 1;
        shape = new RoundRectangle2D.Double(1, 1, s.width - deltaSombra - deltaPulsacion - 1, s.height - deltaSombra - deltaPulsacion - 1, radio, radio);
        pintar(g2);

        super.paint(g);
    }

    @Override
    public boolean contains(int x, int y) {
        return shape != null ? shape.contains(x, y) : false;
    }

    private int getDeltaPulsacion() {
        int delta = (int) (DELTA_PULSACION * getEscala());
        return delta > 0 ? delta : 1;
    }

    private int getDeltaSombra() {
        int delta = (int) (DELTA_SOMBRA * getEscala());
        return delta > 0 ? delta : 1;
    }
}
