package es.jbp.comun.utiles.geometria;

/**
 * Representa un ángulo. Almacena su valor tanto en grados como en radianes
 * permitiendo su conversión automatica.
 * @author Jorge Berjano
 */
public class Angulo implements Cloneable {
    private double grados;
    private double radianes;
    private static final double dosPI = 2 * Math.PI;
    
    /** Creates a new instance of Angulo */
    public Angulo() {
    }
    
    public Angulo(double grados) {
        setGrados(grados);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    
    public String toString() {
        String str = grados + "º";
        return str;
    }
    
    public void setRadianes(double radianes) {
        while (radianes < 0)
            radianes = radianes + dosPI;
        while (radianes >= dosPI)
            radianes = radianes - dosPI;
        
        this.radianes = radianes;
        this.grados = radianes * 360.0 / dosPI;  
    }
    
    public double getRadianes() {
        return radianes;
    }
    
    public void setGrados(double grados) {
        grados = grados % 360;
        if (grados < 0)
            grados = grados + 360.0;
        
        this.radianes = grados * dosPI / 360.0;
        this.grados = grados;
    } 
    
    public double getGrados() {
        return grados;
    }
    
    public double getGradosSigno() {
        if (grados < 180)
            return grados;
        else
            return grados - 360;
    }
    
    public Angulo sumar(Angulo angulo) {
        return new Angulo(grados + angulo.getGrados());
    }
    
    public Angulo restar(Angulo angulo) {
        return new Angulo(grados - angulo.getGrados());
    }
    
    public boolean estaEntre(Angulo angulo1, Angulo angulo2) {
        boolean esCierto;
      
        if (angulo1.getGrados() < angulo2.getGrados())
            esCierto = angulo1.getGrados() < this.getGrados() &&
                this.getGrados() < angulo2.getGrados();
        else
            esCierto = (this.getGrados() < angulo2.getGrados()) ||
                    (angulo1.getGrados() < this.getGrados());
       
       return esCierto;
    }

    public static Angulo anguloMedio(Angulo angulo1, Angulo angulo2) {
        double grados = (angulo2.getGrados() + angulo1.getGrados()) / 2;
        if (angulo1.getGrados() > angulo2.getGrados())
            grados = grados + 180.0;
        return new Angulo(grados);
    }
}
