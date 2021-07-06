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
public class FechaHoraTest {

    public FechaHoraTest() {
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

    @Test
    public void testDiferenciaDias1() {
        Fecha f1 = new Fecha(18, 12, 2017);
        Fecha f2 = new Fecha(14, 05, 2018);

        int dias = f2.diferenciaDias(f1);
        assertEquals(147, dias);
    }

    @Test
    public void testDiferenciaDiasFinAno() {

        Fecha f1 = new Fecha(31, 12, 2017);
        Fecha f2 = new Fecha(1, 1, 2018);

        //Fecha f1 = new Fecha(1, 10, 2018);
        //Fecha f2 = new Fecha(31, 10, 2018);
        long difms = f2.toEpochMilli() - f1.toEpochMilli();
        long dias = difms / (1000 * 60 * 60 * 24);
        assertEquals(1, dias);
    }

    @Test
    public void testDiferenciaMarzoMarzo() {

        Fecha f1 = new Fecha(1, 3, 2018);
        Fecha f2 = new Fecha(31, 3, 2018);
        int dias = f2.diferenciaDias(f1);
        assertEquals(30, dias);
    }

    @Test
    public void testDiferenciaDiasMarzo() {

        Fecha f1 = new Fecha(1, 3, 2018);
        Fecha f2 = new Fecha(31, 3, 2018);

        long difms = f2.toEpochMilli() - f1.toEpochMilli();
        long dias = difms / (1000 * 60 * 60 * 24);
        assertEquals(30, dias);
    }

    @Test
    public void testDiferenciaDiasOctubre() {

        Fecha f1 = new Fecha(1, 10, 2018);
        Fecha f2 = new Fecha(31, 10, 2018);
        int dias = f2.diferenciaDias(f1);
        assertEquals(30, dias);

    }

//    @Test
//    public void testHorasPorDia() {
//        
//        Fecha f1 = new Fecha(18, 12, 2017);
//        for (int i = 0; i < 147; i++) {
//            Fecha f2 = new Fecha(f1);
//            f2.sumarDias(1);
//            int horas = f2.diferenciaHoras(f1);
//            if (horas == 23) {
//                System.out.println("Cambio horario: " + f1);
//            }
//            assertEquals(24, horas);
//            f1 = f2;
//        }
//    }
}
