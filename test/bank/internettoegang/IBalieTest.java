/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
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
public class IBalieTest {
    
    public IBalieTest() {
    }
    
    IBank bank;
    IBalie balie;
    String accountName;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
            bank = new Bank("testBank");
            balie = new Balie(bank);
            
            accountName = balie.openRekening("Gerard", "Amstelveen", "w4cht");
        } catch (RemoteException ex) {
            Logger.getLogger(IBalieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }
    
     /**
     * Test of openRekening method, of class IBalie.
     * This test tries to open an account without a name.
     * @throws java.lang.Exception
     */
    @Test
    public void testOpenRekening() throws Exception {
        String naam = "Peter";
        String plaats = "Alkmaar";
        String wachtwoord = "w00rd";
        String result = balie.openRekening(naam, plaats, wachtwoord);
        assertNotNull(result);
    }

    /**
     * Test of openRekening method, of class IBalie.
     * This test tries to open an account without a name.
     * @throws java.lang.Exception
     */
    @Test
    public void testOpenRekeningNoName() throws Exception {
        String naam = "";
        String plaats = "Alkmaar";
        String wachtwoord = "w00rd";
        String expResult = null;
        String result = balie.openRekening(naam, plaats, wachtwoord);
        assertEquals(expResult, result);
    }

        /**
     * Test of openRekening method, of class IBalie.
     * This test tries to open an account without a place.
     * @throws java.lang.Exception
     */
    @Test
    public void testOpenRekeningNoPlace() throws Exception {
        String naam = "Peter";
        String plaats = "";
        String wachtwoord = "w00rd";
        String expResult = null;
        String result = balie.openRekening(naam, plaats, wachtwoord);
        assertEquals(expResult, result);
    }
    
     /**
     * Test of openRekening method, of class IBalie.
     * This test tries to open an account without a too short password.
     * @throws java.lang.Exception
     */
    @Test
    public void testOpenRekeningShortPassword() throws Exception {
        String naam = "Peter";
        String plaats = "Alkmaar";
        String wachtwoord = "00p";
        String expResult = null;
        String result = balie.openRekening(naam, plaats, wachtwoord);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of openRekening method, of class IBalie.
     * This test tries to open an account without a too long password.
     * @throws java.lang.Exception
     */
    @Test
    public void testOpenRekeningLongPassword() throws Exception {
        String naam = "Peter";
        String plaats = "Alkmaar";
        String wachtwoord = "00pst00l0ngPassword";
        String expResult = null;
        String result = balie.openRekening(naam, plaats, wachtwoord);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of logIn method, of class IBalie.
     * this test tries to log in with an existing account.
     * @throws java.lang.Exception
     */
    @Test
    public void testLogIn() throws Exception {     
        String accountnaam = accountName;
        String wachtwoord = "w4cht";
        IBankiersessie result = balie.logIn(accountnaam, wachtwoord);
        assertNotNull(result);
    }

    /**
     * Test of logIn method, of class IBalie.
     * this test tries to log in with non existing account.
     * @throws java.lang.Exception
     */
    @Test
    public void testLogInNoName() throws Exception {
        String accountnaam = "";
        String wachtwoord = "w4cht";
        IBankiersessie expResult = null;
        IBankiersessie result = balie.logIn(accountnaam, wachtwoord);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of logIn method, of class IBalie.
     * this test tries to log in with non existing account.
     * @throws java.lang.Exception
     */
    @Test
    public void testLogInWrongPassword() throws Exception {
        String accountnaam = accountName;
        String wachtwoord = "00ps";
        IBankiersessie expResult = null;
        IBankiersessie result = balie.logIn(accountnaam, wachtwoord);
        assertEquals(expResult, result);
    }
}
