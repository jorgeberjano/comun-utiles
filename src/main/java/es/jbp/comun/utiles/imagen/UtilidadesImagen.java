package es.jbp.comun.utiles.imagen;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;
import es.jbp.comun.utiles.geometria.Direccion;
import es.jbp.comun.utiles.geometria.UtilidadesGeometria;

/**
 * Utilidades relacionadas con imagenes
 *
 * @author Jorge Berjano
 */
public class UtilidadesImagen {
    
    public static final int NO_INVERTIR = 0;
    public static final int INVERTIR_EJE_X = 1;
    public static final int INVERTIR_EJE_Y = 2;

    private UtilidadesImagen() {
    }

    /**
     * Captura un componente en una imagen.
     *
     * @param componente
     * @return
     */
    public static Image capturarImagen(Component componente) {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Crea una imagen que soporta pixels transparentes
        BufferedImage imagen = gc.createCompatibleImage(componente.getWidth(), componente.getHeight(), Transparency.OPAQUE);

        // Se obtiene el contexto grafico de la imagen
        Graphics2D g = imagen.createGraphics();

        // Se pinta el componente en el contexto gráfico de la imagen
        componente.paint(g);

        return imagen;
    }

    /**
     * Carga una imagen referemciada por una url. Sirve, entre otras cosas, para
     * cargar imagenes que est�n dentro de un archivo .jar.
     *
     * @param url La url de la imagen, por ejemplo:
     * getClass().getResource("/com/.../imagen.jpg")
     * @return La imagen cargada.
     */
    public static Image cargarImagen(URL url) throws Exception {
        return new ImageIcon(url).getImage();
    }

    public static Image cargarImagen(String ruta, int anchoMaximo, int altoMaximo) throws Exception {
        Image imagen = cargarImagen(ruta);
        imagen = escalarImagen(imagen, anchoMaximo, altoMaximo);
        return imagen;
    }

    /**
     * Escala la imagen que se pasa como parametro en funcion de su tamaño y del
     * tamaño del contenedor.
     *
     * @param imagen
     * @param anchoMaximo : ancho del contenedor
     * @param altoMaximo : alto del contenedor
     */
    public static Image escalarImagen(Image imagen, int anchoMaximo, int altoMaximo) {
        return escalarImagen(imagen, anchoMaximo, altoMaximo, Image.SCALE_FAST);
    }
    
    /**
     * Escala la imagen que se pasa como parametro en funcion de su tamaño y del
     * tamaño del contenedor.
     *
     * @param imagen
     * @param anchoMaximo : ancho del contenedor
     * @param altoMaximo : alto del contenedor
     * 
     */
    public static Image escalarImagen(Image imagen, int anchoMaximo, int altoMaximo, int hints) {

        int anchoImagen = imagen.getWidth(null);
        int altoImagen = imagen.getHeight(null);

        if ((altoImagen > altoMaximo || anchoImagen > anchoMaximo) && anchoMaximo > 0 && altoMaximo > 0) {
            Rectangle2D rectanguloContenedor = new Rectangle2D.Double(0, 0, anchoMaximo, altoMaximo);
            Rectangle2D rectanguloImagen = UtilidadesGeometria.rectanguloInscrito(rectanguloContenedor, anchoImagen, altoImagen);
            imagen = imagen.getScaledInstance((int) rectanguloImagen.getWidth(), (int) rectanguloImagen.getHeight(), hints);
        }
        return imagen;
    }

    public static BufferedImage invertirImagen(Image imagen, int eje) {
        int tipo = BufferedImage.TYPE_INT_RGB;
        if (imagen instanceof BufferedImage) {
            tipo = ((BufferedImage) imagen).getType();
        }
        
        int width = imagen.getWidth(null);
        int height = imagen.getHeight(null);
        
        BufferedImage imagenProcesada = new BufferedImage(imagen.getWidth(null), imagen.getHeight(null),
                tipo);

        Graphics2D g = imagenProcesada.createGraphics();
        if (eje == INVERTIR_EJE_X) {
            g.drawImage(imagen, 0, 0, width, height, 0, height, width, 0, null);
        } else if (eje == INVERTIR_EJE_Y) {
            g.drawImage(imagen, 0, 0, width, height, width, 0, 0, height, null);
        } else {
            g.drawImage(imagen, 0, 0, width, height, width, height, 0, 0, null);
        }
        return imagenProcesada;
    }

    public static BufferedImage rotarImagen(Image imagen, int rotacion) {
        return rotarImagen(imagen, rotacion, NO_INVERTIR);
    }
    
    public static BufferedImage rotarImagen(Image imagen, int rotacion, int invertir) {
        int anchoProcesado, altoProcesado;

        int anchoOriginal = imagen.getWidth(null);
        int altoOriginal = imagen.getHeight(null);
        int desp = (anchoOriginal - altoOriginal) / 2;
        int dx = 0, dy = 0;
        if (rotacion == 90 || rotacion == 270) {
            anchoProcesado = altoOriginal;
            altoProcesado = anchoOriginal;
            dx = -desp;
            dy = desp;
        } else {
            anchoProcesado = anchoOriginal;
            altoProcesado = altoOriginal;
        }

        int tipo = BufferedImage.TYPE_INT_RGB;
        if (imagen instanceof BufferedImage) {
            tipo = ((BufferedImage) imagen).getType();
        }

        BufferedImage imagenProcesada = new BufferedImage(anchoProcesado, altoProcesado, tipo);
        Graphics2D g2d = imagenProcesada.createGraphics();
        g2d.rotate(rotacion * Math.PI / 180.0, anchoProcesado / 2, altoProcesado / 2);
        g2d.translate(dx, dy);
        
        switch (invertir) {
            case INVERTIR_EJE_X:
                g2d.drawImage(imagen, 0, 0, anchoOriginal, altoOriginal, 0, altoOriginal, anchoOriginal, 0, null);
                break;
            case INVERTIR_EJE_Y:
                g2d.drawImage(imagen, 0, 0, anchoOriginal, altoOriginal, anchoOriginal, 0, 0, altoOriginal, null);
                break;
            default:
                g2d.drawImage(imagen, 0, 0, anchoOriginal, altoOriginal, null);
                break;
        }        
        
        return imagenProcesada;
    }

    public static Image cargarImagen(InputStream is) throws Exception {
        Image imagen = ImageIO.read(is);
        is.close();
        return imagen;
    }

    public static Image cargarImagen(String ruta) throws Exception {
        return cargarImagen(new FileInputStream(ruta));
    }

    public static BufferedImage decodificarImagen(String tipo, String ruta) throws Exception {
        File flujo = new File(ruta);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("tiff").next();
        FileImageInputStream inputStream = new FileImageInputStream(flujo);
        ImageReader reader = ImageIO.getImageReader(writer);
        reader.setInput(inputStream);

        ImageReadParam readParam = reader.getDefaultReadParam();
        RenderedImage imagenRendered = reader.readAsRenderedImage(0, readParam);

        double ancho = imagenRendered.getWidth();
        double alto = imagenRendered.getHeight();

        BufferedImage imagen = new BufferedImage((int) ancho, (int) alto, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) imagen.getGraphics();
        graphics.drawRenderedImage(imagenRendered, new AffineTransform());

        return imagen;
    }

    public static void pintar(Graphics g, Image imagen, double ancho, double alto, Direccion direccion) {

        if (imagen == null) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) g;

        double altoImagen = imagen.getHeight(null);
        double anchoImagen = imagen.getWidth(null);

        Rectangle2D rectanguloImagen;

        double x;
        double y;
        double escala = 1;
        //double escalaAlto = 1;

        if (anchoImagen > ancho || altoImagen > alto) {
            rectanguloImagen = UtilidadesGeometria.rectanguloInscrito(new Rectangle2D.Double(0, 0, ancho, alto), anchoImagen, altoImagen);

            x = rectanguloImagen.getX();
            y = rectanguloImagen.getY();

            escala = rectanguloImagen.getWidth() / anchoImagen;
            //escalaAlto = rectanguloImagen.getHeight() / altoImagen;

            anchoImagen = rectanguloImagen.getWidth();
            altoImagen = rectanguloImagen.getHeight();
        } else {
            x = (ancho - anchoImagen) / 2;
            y = (alto - altoImagen) / 2;
        }

        Rectangle2D rectanguloAlineado = new Rectangle2D.Double(0, 0, ancho, alto);
        UtilidadesGeometria.redimensionarRectangulo(rectanguloAlineado, anchoImagen, altoImagen, direccion);
        x = rectanguloAlineado.getX();
        y = rectanguloAlineado.getY();

        try {
            AffineTransform transformacion = new AffineTransform();
            transformacion.translate(x, y);
            transformacion.scale(escala, escala);

            graphics2D.drawImage(imagen, transformacion, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pintar(Graphics g, Image imagen, double ancho, double alto) {

        if (imagen == null) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) g;

        double altoImagen = imagen.getHeight(null);
        double anchoImagen = imagen.getWidth(null);

        Rectangle2D rectanguloImagen;

        double x;
        double y;
        double escalaAncho = 1;
        double escalaAlto = 1;

        if (anchoImagen > ancho || altoImagen > alto) {
            rectanguloImagen = UtilidadesGeometria.rectanguloInscrito(new Rectangle2D.Double(0, 0, ancho, alto), anchoImagen, altoImagen);
            x = rectanguloImagen.getX();
            y = rectanguloImagen.getY();
            escalaAncho = rectanguloImagen.getWidth() / anchoImagen;
            escalaAlto = rectanguloImagen.getHeight() / altoImagen;
        } else {
            x = (ancho - anchoImagen) / 2;
            y = (alto - altoImagen) / 2;
        }

        try {
            AffineTransform transformacion = new AffineTransform();
            transformacion.translate(x, y);
            transformacion.scale(escalaAncho, escalaAlto);

            graphics2D.drawImage(imagen, transformacion, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Image escalarLienzo(Image imagen, int anchoImagen, int altoImagen, int anchoLienzo, int altoLienzo, Direccion direccion) {

//        imagen = imagen.getScaledInstance(anchoImagen, altoImagen, java.awt.Image.SCALE_SMOOTH);
        
        BufferedImage imagenProcesada = new BufferedImage(anchoLienzo, altoLienzo, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagenProcesada.createGraphics();

        int dx = anchoLienzo - anchoImagen;
        int dy = altoLienzo - altoImagen;
        
        switch (direccion) {
            case CENTRO:
                dx = dx / 2;
                dy = dy / 2;
                break;
            case NORTE:
                dx = dx / 2;
                dy = 0;
                break;
            case NORESTE:
                dy = 0;
                break;
            case ESTE:
                dy = dy / 2;
                break;
            case SURESTE:              
                break;
            case SUR:
                dx = dx / 2;
                break;
            case SUROESTE:
                dx = 0;
                break;
            case OESTE:
                dx = 0;
                dy = dx / 2;
                break;
            case NOROESTE:
                dx = 0;
                dy = 0;
                break;
        }

        
        g.drawImage(imagen, dx, dy, anchoImagen + dx, altoImagen + dy, 0, 0, anchoImagen, altoImagen, null);
    
        return imagenProcesada;
    }
}
