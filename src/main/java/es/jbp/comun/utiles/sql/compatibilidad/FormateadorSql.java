package es.jbp.comun.utiles.sql.compatibilidad;

import es.jbp.comun.utiles.sql.TipoDato;
import java.util.Set;
import es.jbp.comun.utiles.sql.esquema.CampoBd;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaHora;
import es.jbp.comun.utiles.tiempo.FechaHoraMs;

/**
 * Contrato que deben cumplir los formateadores SQL
 *
 * @author jberjano
 */
public interface FormateadorSql {

    String getFechaSql(Fecha fecha);

    String getFechaHoraSql(FechaHora fechaHora);

    String getFechaHoraMsSql(FechaHoraMs fechaHora);

    String getBooleanSql(Boolean aBoolean);

    String getNombreTipoSql(CampoBd campo);

    String getSelectConsultaFechaHora();

    String getFucionNVL(String valor, String valorSiEsNulo);

    String getFechaActual();

    String getFechaHoraActual();

    String getFechaHoraMsActual();

    Set<Integer> getCodigosErrorDesconexion();

    String formatear(Object valor);

    Object convertirValor(Object valor, TipoDato tipo);
    
    Object convertirAlPersistir(Object valor);

    String getContieneTexto(String valor);

    String getCaracterComodin();

    String getOperadorDistinto();

    String getValorSimbolo(String simbolo);

    String getComparacionNoCase(String operador1, String operando, String operador2);
   
    Fecha toFecha(Object valor);
    
    FechaHora toFechaHora(Object valor);
    
    FechaHoraMs toFechaHoraMs(Object valor);
}
