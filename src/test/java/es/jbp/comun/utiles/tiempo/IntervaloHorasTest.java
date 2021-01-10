package es.jbp.comun.utiles.tiempo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jberjano
 */
public class IntervaloHorasTest {
    
    public IntervaloHorasTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of esValida method, of class IntervaloHoras.
     */
    @Test
    public void testHoraMinuto() {
        
       HoraMinuto hm1 = new HoraMinuto(25, 65);
       assertEquals(hm1, new HoraMinuto(02, 05));
       
       hm1.setMinuto(hm1.getMinuto() - 30);
       
       assertEquals(hm1, new HoraMinuto(1, 35));
    }
    
    @Test
    public void testIntervaloHoras1() {
       IntervaloHoras intervalo = new IntervaloHoras();
       
       intervalo.setHoraDesde(new HoraMinuto(23, 00));
       intervalo.setHoraHasta(new HoraMinuto(01, 00));
       
       assertTrue(intervalo.incluye(new HoraMinuto(23, 30)));
       assertTrue(intervalo.incluye(new HoraMinuto(0, 10)));
       assertTrue(intervalo.incluye(new HoraMinuto(1, -10)));
       assertFalse(intervalo.incluye(new HoraMinuto(22, 00)));
       assertFalse(intervalo.incluye(new HoraMinuto(2, 00)));
    }
    
    @Test
    public void testIntervaloHoras2() {
       IntervaloHoras intervalo = new IntervaloHoras();
       
       intervalo.setHoraDesde(new HoraMinuto(01, 00));
       intervalo.setHoraHasta(new HoraMinuto(23, 00));       
       
       assertFalse(intervalo.incluye(new HoraMinuto(23, 30)));
       assertFalse(intervalo.incluye(new HoraMinuto(0, 10)));
       assertFalse(intervalo.incluye(new HoraMinuto(1, -10)));
       assertTrue(intervalo.incluye(new HoraMinuto(22, 00)));
       assertTrue(intervalo.incluye(new HoraMinuto(2, 00)));
    }
    
}
