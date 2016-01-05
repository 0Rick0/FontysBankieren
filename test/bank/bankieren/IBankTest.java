/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import fontys.util.NumberDoesntExistException;
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
    
    @Test
    public void testOpenRekeningInvallid(){
        System.out.println("openRekeningInvallid");
        assertEquals("Empty name",bank.openRekening("", "test"),-1);
        assertEquals("Empty city",bank.openRekening("test", ""),-1);
        assertEquals("Empty name and city",bank.openRekening("", ""),-1);
        assertEquals("Null name",bank.openRekening(null, "test"),-1);
        assertEquals("Null city",bank.openRekening("test", null),-1);
        assertEquals("Null name and city",bank.openRekening(null, null),-1);
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
    
    @Test
    public void testMaakOverAbuse(){
        int bron = bank.openRekening("TestAbuse1", "Test");
        int target = bank.openRekening("TestAbuse2", "Test");
        
        //bron==target
        try{
            assertFalse(bank.maakOver(bron, bron, new Money(10L,"€")));
            fail();//should not be reached
        }catch(RuntimeException ex){
            //all is good :)
        } catch (NumberDoesntExistException ex) {
            Logger.getLogger(IBankTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();//dafuq happened!
        }
        
        //negativ money
        try{
            assertFalse(bank.maakOver(bron, target, new Money(-10L,"€")));
            fail();//should not be reached
        }catch(RuntimeException ex){
            //all is good :)
        } catch (NumberDoesntExistException ex) {
            Logger.getLogger(IBankTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();//dafuq happened!
        }
        
        //non existend source
        try{
            assertFalse(bank.maakOver(-1, target, new Money(10L,"€")));
            fail();//should not be reached
        }catch(RuntimeException ex){
            Logger.getLogger(IBankTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();//dafuq happened!
        } catch (NumberDoesntExistException ex) {
            
            //all is good
        }
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
