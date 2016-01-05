/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import fontys.util.NumberDoesntExistException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class IBankTest {
    
    private static IBank bank;
    
    public IBankTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        bank = new Bank("testBank");
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        bank = null;
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of openRekening method, of class IBank.
     */
    @Test
    public void testOpenRekening() {
        System.out.println("openRekening");
        String naam = "TestUser";
        String plaats = "TestLocation";
        IBank instance = bank;
        int result = instance.openRekening(naam, plaats);
        IRekening rekening = bank.getRekening(result);
        assertTrue("Check if rekening owner = name",rekening.getEigenaar().getNaam().equals(naam));
    }

    /**
     * Test of maakOver method, of class IBank.
     */
    @Test
    public void testMaakOver() throws Exception {
        System.out.println("maakOver");
        int bron = bank.openRekening("Test1", "Test");
        int bestemming = bank.openRekening("Test2", "Test");
        Money bedrag = new Money(100L,"€");
        boolean result = bank.maakOver(bron, bestemming, bedrag);
        IRekening bronR = bank.getRekening(bron);
        IRekening bestemmingR = bank.getRekening(bestemming);
        assertTrue("Succesvol overgeboekt",result);
        assertEquals("100L eraf",-100L,bronR.getSaldo().getCents());
        assertEquals("100L erbij",100L,bestemmingR.getSaldo().getCents());
    }
    
    @Test(expected=NumberDoesntExistException.class)
    public void testMaakOverNotExist() throws NumberDoesntExistException{
        System.out.println("maakOver");
        int bron = bank.openRekening("Test1", "Test");
        int bestemming = bank.openRekening("Test2", "Test");
        Money bedrag = new Money(100L,"€");
        boolean result = bank.maakOver(bron, 100, bedrag);
        fail("No error thrown!");
    }
    
    @Test(expected=RuntimeException.class)
    public void testMaakOverIvallidMoney() throws NumberDoesntExistException{
        System.out.println("maakOver");
        int bron = bank.openRekening("Test1", "Test");
        int bestemming = bank.openRekening("Test2", "Test");
        Money bedrag = new Money(100L,"");
        fail("No error thrown!");
    }

    /**
     * Test of getRekening method, of class IBank.
     */
    @Test
    public void testGetRekening() {
        int r = bank.openRekening("Test","TestGetRekening");
        IRekening rekening = bank.getRekening(r);
        assertEquals("namen het zelfde",rekening.getEigenaar().getNaam(),"Test");
    }

    /**
     * Test of getName method, of class IBank.
     */
    @Test
    public void testGetName() {
        assertEquals("check naam","testBank",bank.getName());
    }
    
}
