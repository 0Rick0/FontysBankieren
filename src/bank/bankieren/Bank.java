package bank.bankieren;

import bank.centralbank.ICentralBank;
import fontys.util.*;
import java.rmi.AccessException;
import java.rmi.NotBoundException;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank extends UnicastRemoteObject implements IBank, IBankCentraleBank {

    /**
     *
     */
    private static final long serialVersionUID = -8728841131739353765L;
    private final Map<Integer, IRekeningTbvBank> accounts;
    private final Collection<IKlant> clients;
    private int nieuwReknr;
    private final String name;
    private ICentralBank centralBank;
    private Registry registry;
    private boolean registered = false;
    
    public Bank(String name) throws RemoteException {
        accounts = new HashMap<>();
        clients = new ArrayList<>();
        nieuwReknr = 100000000;
        this.name = name;
        
        try {
            registry = LocateRegistry.getRegistry("localhost", 420);
            centralBank = (ICentralBank)registry.lookup("CentraleBank");
        } catch (RemoteException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        register();
    }

    private void register(){
        try {
            centralBank.registreerBank(name, (IBankCentraleBank)this);
        } catch (RemoteException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public int openRekening(String name, String city) {
        if (name == null || city == null) {
            return -1;
        }
        if (name.equals("") || city.equals("")) {
            return -1;
        }

        IKlant klant = getKlant(name, city);
        synchronized (accounts) {
            int nextRekening;
            try {
                nextRekening = centralBank.registreerRekening(this.name);
            } catch (RemoteException ex) {
                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
            if(nextRekening == -1) return -1;
            IRekeningTbvBank account = new Rekening(nextRekening, klant, Money.EURO);
            accounts.put(nextRekening, account);
            return nextRekening;
        }
    }

    private IKlant getKlant(String name, String city) {
        for (IKlant k : clients) {//todo synchronization size changed while looping?
            if (k.getNaam().equals(name) && k.getPlaats().equals(city)) {
                return k;
            }
        }
        IKlant klant = new Klant(name, city);
        synchronized (clients) {
            clients.add(klant);
        }
        return klant;
    }

    @Override
    public IRekening getRekening(int nr) {
        return accounts.get(nr);
    }

    @Override
    public boolean maakOver(int source, int destination, Money money)
            throws NumberDoesntExistException {
        if (source == destination) {
            throw new RuntimeException(
                    "cannot transfer money to your own account");
        }
        if (!money.isPositive()) {
            throw new RuntimeException("money must be positive");
        }

        IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
        if (source_account == null) {
            throw new NumberDoesntExistException("account " + destination
                    + " unknown at " + name);
        }

        Money negative = Money.difference(new Money(0, money.getCurrency()),
                money);
        boolean success;
        synchronized (source_account) {
            success = source_account.muteer(negative);
        }
        if (!success) {
            return false;
        }

        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
        if (dest_account == null) {
            if(centralBank != null)
            {
                try {
                    success = centralBank.maakOver(source, destination, money)==0;
                } catch (RemoteException ex) {
                    Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                throw new NumberDoesntExistException("account " + destination
                    + " unknown at " + name);
            }
        }else{
            synchronized (dest_account) {
                success = dest_account.muteer(money);
            }
        }

        if (!success) // rollback
        {
            synchronized (source_account) {
                source_account.muteer(money);
            }
        }
        return success;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addListener(int rekeningNr, IRekeningUpdateListener listener) {
        getRekening(rekeningNr).addListener(listener);
    }

    @Override
    public void removeListener(int rekeningNr, IRekeningUpdateListener listener) {
        getRekening(rekeningNr).removeListener(listener);
    }

    @Override
    public boolean maakOverRemote(int source, int destination, Money money) throws RemoteException {
        
        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
        
        synchronized (dest_account) {
            return dest_account.muteer(money);
        }
    }

}
