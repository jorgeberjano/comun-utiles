/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CadenaVaciaTest {
    
    public CadenaVaciaTest() {
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
    public void testCadenaVacia() {
        
        String s1 = "";
        String s2 = new String();
        String s3 = new String(s1);
        
        assertTrue(s1 == "");
        assertFalse(s2 == "");
        assertFalse(s3 == "");
    }
}
