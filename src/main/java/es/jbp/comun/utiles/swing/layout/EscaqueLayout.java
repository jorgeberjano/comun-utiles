package es.jbp.comun.utiles.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Layout para distribuir componentes swing en forma de cuadricula. Se debe
 * especificar el tamaño de los componentes y el componente calcula el
 * número de filas y columnas.
 *
 * @author Jorge Berjano
 */
public class EscaqueLayout implements LayoutManager {

    public static int AJUSTAR_ANCHO = 0;
    public static int AJUSTAR_ALTO = 1;
    public static int AJUSTAR_SUPERFICIE = 2;

    private int filas;
    private int columnas;
    private int anchoCelda = 100;
    private int altoCelda = 100;
    private int margen;

    private int modo = AJUSTAR_ANCHO;

    public EscaqueLayout() {
    }

    public EscaqueLayout(int modo) {
        this.modo = modo;
    }

    public EscaqueLayout(int modo, int anchoCelda, int altoCelda) {
        this.modo = modo;
        this.anchoCelda = anchoCelda;
        this.altoCelda = altoCelda;
    }

    @Override
    public void addLayoutComponent(String name, Component component) {
    }

    @Override
    public void removeLayoutComponent(Component component) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int anchoDeseado = anchoCelda * columnas + margen * (columnas + 1);
        int altoDeseado = altoCelda * filas + margen * (filas + 1);
        
        if (modo == AJUSTAR_ANCHO) {
            anchoDeseado = anchoCelda;
        } else if (modo == AJUSTAR_ALTO) {
            altoDeseado = altoCelda;
        }
        
        Insets insets = parent.getInsets();
        anchoDeseado += insets.right + insets.left;
        return new Dimension(anchoDeseado, altoDeseado);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return parent.getSize();
    }

    @Override
    public void layoutContainer(Container parent) {
        
        Insets insets = parent.getInsets();
        int ox = insets.left;
        int oy = insets.top;
        
        recalcularFilasColumnas(parent);
        int i = 0, j = 0;
        for (Component componente : parent.getComponents()) {
            componente.setLocation(ox + i * (anchoCelda + margen), oy + j * (altoCelda + margen));
            componente.setSize(anchoCelda, altoCelda);
            if (modo == AJUSTAR_ALTO) {
                j++;
                if (j >= filas) {
                    i++;
                    j = 0;
                }
            } else {
                i++;
                if (i >= columnas) {
                    j++;
                    i = 0;
                }
            }
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    private void recalcularFilasColumnas(Container parent) {
        Component[] componentes = parent.getComponents();

        Dimension size = parent.getSize();
        Insets insets = parent.getInsets();
        
        double ancho = size.getWidth() - insets.left - insets.right;
        double alto = size.getHeight() - insets.top - insets.bottom;

        if (modo == AJUSTAR_ANCHO) {
            columnas = (int) ((ancho + margen) / (anchoCelda + margen));
            filas = (int) Math.ceil((double) componentes.length / (double) columnas);

        } else if (modo == AJUSTAR_ALTO) {
            filas = (int) ((alto + margen) / (altoCelda + margen));
            columnas = (int) Math.ceil((double) componentes.length / (double) filas);

        } else {
            filas = (int) Math.sqrt(componentes.length);
            columnas = (int) Math.ceil((double) componentes.length / (double) filas);
        }

    }

    public int getAnchoCelda() {
        return anchoCelda;
    }

    public void setAnchoCelda(int anchoCelda) {
        this.anchoCelda = anchoCelda;
    }

    public int getAltoCelda() {
        return altoCelda;
    }

    public void setAltoCelda(int altoCelda) {
        this.altoCelda = altoCelda;
    }

    public int getMargen() {
        return margen;
    }

    public void setMargen(int margen) {
        this.margen = margen;
    }

}
