/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centralbank;

import bank.bankieren.IBankCentraleBank;
import bank.bankieren.Money;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class CentralBank extends UnicastRemoteObject implements ICentralBank {

    private Map<String, IBankCentraleBank> banken;
    private Map<Integer, String> rekeningen;
    private int newNummer = 100000;

    public CentralBank() throws RemoteException {
        banken = new HashMap<>();
        rekeningen = new HashMap<>();
    }

    @Override
    public int maakOver(int source, int target, Money money) throws RemoteException {
        Optional<Integer> val = rekeningen.keySet().stream().filter((i)->i == target).findFirst();
        if (!val.isPresent()) {
            return 1;
        }
        IBankCentraleBank b = banken.get(rekeningen.get(val.get()));
        
        if(!b.maakOverRemote(source, target, money)){
            return 2;
        }
        return 0;
    }

    @Override
    public synchronized int registreerRekening(String bank) throws RemoteException {
       
        Optional<String> bankName = banken.keySet().stream().filter((b)->b.equalsIgnoreCase(bank)).findFirst();
        if(!bankName.isPresent()){
            return -1;//bank doesn't exists
        }
        
        rekeningen.put(newNummer, bankName.get());
        
        return newNummer++;//first use then increment
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
