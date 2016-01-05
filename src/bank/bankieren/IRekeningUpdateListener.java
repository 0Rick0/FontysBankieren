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
 * @author Rick Rongen, www.R-Ware.tk
 */
public interface IRekeningUpdateListener extends Remote {
    public void updateBalance(Money value) throws RemoteException;
}
