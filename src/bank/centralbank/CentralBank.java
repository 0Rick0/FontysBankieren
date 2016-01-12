/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centralbank;

import bank.bankieren.IBankCentraleBank;
import bank.bankieren.Money;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class CentralBank extends UnicastRemoteObject implements ICentralBank {

    private Map<String, IBankCentraleBank> banken;
    private Map<Integer, String> rekeningen;
    private Registry registry;

    public CentralBank() throws RemoteException {
        banken = new HashMap<>();
        rekeningen = new HashMap<>();
        registry = LocateRegistry.createRegistry(420);
        try {
            registry.bind("CentralBank", this);
        } catch (AlreadyBoundException | AccessException ex) {
            Logger.getLogger(CentralBank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int maakOver(int source, int target, Money money) throws RemoteException {
        Optional<Integer> val = rekeningen.keySet().stream().filter((i)->i == target).findFirst();
        if (!val.isPresent()) {
            return 1;
        }
        IBankCentraleBank b = banken.get(rekeningen.get(val.get()));
        //+maakOverRemote(source : int, destenation : int, money : bank.bankieren.Money) : boolean
        if(!false/*b.maakOverRemote(source,target,money)*/){
            return 2;
        }
        return 0;
    }

    @Override
    public boolean registreerRekening(int rekening, String bank) throws RemoteException {
        if(rekeningen.containsKey(rekening)){
            return false;//rekening already exists
        }
        Optional<String> bankName = banken.keySet().stream().filter((b)->b.equalsIgnoreCase(bank)).findFirst();
        if(!bankName.isPresent()){
            return false;//bank doesn't exists
        }
        
        rekeningen.put(rekening, bankName.get());
        
        return true;
    }

    @Override
    public boolean onregistreerRekening(int rekening, String bank) throws RemoteException {
        if(!rekeningen.containsKey(rekening)){
            return false;//rekening doesn't exists
        }
        if(!rekeningen.get(rekening).equalsIgnoreCase(bank)){
            return false;//not the same bank
        }
        rekeningen.remove(rekening);
        return true;
    }

    @Override
    public boolean registreerBank(String naam, IBankCentraleBank bank) throws RemoteException {
        if(banken.keySet().stream().anyMatch((b)->b.equals(naam))){
            return false;
        }
        banken.put(naam, bank);
        return true;
    }

    @Override
    public boolean onregistreerBank(String naam, IBankCentraleBank bank) throws RemoteException {
        Optional<String> oBankNaam = banken.keySet().stream().filter((b)->b.equalsIgnoreCase(naam)).findFirst();
        if(!oBankNaam.isPresent()){
            return false;//bank not registered
        }
        if(!banken.get(oBankNaam.get()).equals(bank)){
            return false;//not same bank
        }
        banken.remove(oBankNaam.get());
        return true;
    }

}
