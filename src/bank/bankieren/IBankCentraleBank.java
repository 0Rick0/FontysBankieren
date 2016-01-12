/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Martijn
 */
public interface IBankCentraleBank extends Remote {

    /**
     * Method that returns the name of the bank.
     * @return the name of the bank.
     */
//    String getName();
    
    /**
     * Method that transfers money from one account to the other.
     * @param source, The account where the money comes from.
     * @param destination, The account where the money will be transfered to.
     * @param money, The money that will be transfered.
     * @return a boolean that returns true if the transfer was successful. 
     * it returns false if it fails.
     */
    boolean maakOverRemote(int source, int destination, Money money) throws RemoteException;
}
