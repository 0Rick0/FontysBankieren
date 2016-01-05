/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.rmi.RemoteException;
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
 * @author Martijn
 */
public class IBankiersessieTest {
    
    public IBankiersessieTest() {
    }
    
    IBankiersessie sessie;
    IBankiersessie sessieRecieve;
    IBank bank;
    int rekeningReceive;
    int rekeningUser;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
            bank = new Bank("test");
            rekeningReceive = bank.openRekening("Peter", "Alkmaar");
            sessieRecieve = new Bankiersessie(rekeningReceive, bank);
            
            rekeningUser = bank.openRekening("Gerard", "Alkmaar");
        } catch (RemoteException ex) {
            Logger.getLogger(IBankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isGeldig method, of class IBankiersessie.
     * This test will return true;
     */
    @Test
    public void testIsGeldigTrue() throws Exception {
        System.out.println("isGeldig");
        sessie = new Bankiersessie(1, bank);
        boolean expResult = true;
        boolean result = sessie.isGeldig();
        assertEquals(expResult, result);
    }

        @Test
    public void testIsGeldigFalse() throws Exception {
        System.out.println("isGeldig");
        sessie = new Bankiersessie(1, bank);
        boolean expResult = false;
        Thread.sleep(2100);
        boolean result = sessie.isGeldig();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of maakOver method, of class IBankiersessie.
     */
    @Test
    public void testMaakOver() throws Exception {
        System.out.println("maakOver");
        sessie = new Bankiersessie(rekeningUser, bank);
        int bestemming = rekeningReceive;
        Money bedrag = new Money(100, Money.EURO);
        boolean expResult = true;
        boolean result = sessie.maakOver(bestemming, bedrag);
        assertEquals(expResult, result);
    }

        /**
     * Test of maakOver method, of class IBankiersessie.
     */
    @Test(expected=RuntimeException.class)
    public void testMaakOverToSelf() throws Exception {
        System.out.println("maakOver");
        sessie = new Bankiersessie(rekeningUser, bank);
        int bestemming = rekeningUser;
        Money bedrag = new Money(100, Money.EURO);
        boolean expResult = true;
        boolean result = sessie.maakOver(bestemming, bedrag);
        assertEquals(expResult, result);
    }
    
        /**
     * Test of maakOver method, of class IBankiersessie.
     */
    @Test(expected=RuntimeException.class)
    public void testMaakOverNegative() throws Exception {
        System.out.println("maakOver");
        sessie = new Bankiersessie(rekeningUser, bank);
        int bestemming = rekeningReceive;
        Money bedrag = new Money(-100, Money.EURO);
        boolean expResult = true;
        boolean result = sessie.maakOver(bestemming, bedrag);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of maakOver method, of class IBankiersessie.
     */
    @Test(expected=InvalidSessionException.class)
    public void testMaakOverExpire() throws Exception {
        System.out.println("maakOver");
        sessie = new Bankiersessie(rekeningUser, bank);
        Thread.sleep(2200);
        int bestemming = rekeningReceive;
        Money bedrag = new Money(100, Money.EURO);
        boolean expResult = true;
        boolean result = sessie.maakOver(bestemming, bedrag);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of logUit method, of class IBankiersessie.
     */
    @Test
    public void testLogUit() throws Exception {
        System.out.println("logUit");
        sessie = new Bankiersessie(1, bank);
        sessie.logUit();
    }

    /**
     * Test of getRekening method, of class IBankiersessie.
     */
    @Test
    public void testGetRekening() throws Exception {
        System.out.println("getRekening");
        sessie = new Bankiersessie(1, bank);
        IRekening expResult = null;
        IRekening result = sessie.getRekening();
        assertEquals(expResult, result);
    }    
}
