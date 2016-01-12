/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centralbank;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class Run {
    public static void main(String[] args) throws RemoteException, NotBoundException{
        CentralBank bank = new CentralBank();
        Registry reg = LocateRegistry.createRegistry(420);
        reg.rebind("CentraleBank", bank);
        Scanner scan = new Scanner(System.in);
        System.out.println("Type quit to stop");
        while(!scan.nextLine().equalsIgnoreCase("quit")){
            
        }
        reg.unbind("centralebank");
        
        
    }
}
