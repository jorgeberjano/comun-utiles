package es.jbp.comun.utiles.tiempo;

/**
 * Representa un intervalo desde una hora hasta otra hora.
 *
 * @author jberjano
 */
public class IntervaloHoras implements Cloneable {

    private HoraMinuto horaDesde;
    private HoraMinuto horaHasta;

    public IntervaloHoras() {
    }
    
    public IntervaloHoras(HoraMinuto horaDesde, HoraMinuto horaHasta) {
        this.horaDesde = horaDesde;
        this.horaHasta = horaHasta;
    }

    public boolean esNulo() {
        return horaDesde.equals(horaHasta);
    }

    public HoraMinuto getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(HoraMinuto HoraDesde) {
        this.horaDesde = HoraDesde;
    }

    public HoraMinuto getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(HoraMinuto HoraHasta) {
        this.horaHasta = HoraHasta;
    }
    
    public boolean incluyeMedianoche() {
        return horaDesde.getMinutoAbsoluto() > horaHasta.getMinutoAbsoluto();
    }

    public boolean incluye(HoraMinuto hm) {        
        if (esNulo()) {
            return false;
        }        
        if (incluyeMedianoche()) {
            return hm.comparar(horaDesde) >= 0 || hm.comparar(horaHasta) <= 0;
        } else {
            return hm.comparar(horaDesde) >= 0 && hm.comparar(horaHasta) <= 0;
        }
    }

    @Override
    public String toString() {
        return "de " + horaDesde + " a " + horaHasta;
    }
    
    
    
}
