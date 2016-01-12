/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centralbank;

import bank.bankieren.IBankCentraleBank;
import bank.bankieren.Money;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public interface ICentralBank extends Remote {
    
    /**
     * maak geld over van een rekening naar een andere
     * @param source de bron rekening
     * @param target de doel rekening
     * @param money de hoeveelheid doe overgeboekt moet worden
     * @return 0 = OK, 1=rekening niet gevonden, 2=overboeking afgewezen bij de remote bank
     * @exception RemoteException als er een RMI fout optreed
     */
    public int maakOver(int source, int target, Money money) throws RemoteException;
    
    /**
     * registreer een rekening op de centrale bank
     * @param rekening de rekening die geregistreerd moet worden
     * @param bank de bank waar de rekening bestaat
     * @return of de rekening kan worden toegevoegd, het kan bijvoorbeeld niet als de bank niet geregistreerd is of als de rekening al bestaat
     * @exception RemoteException als er een RI fout optreed
     */
    public boolean registreerRekening(int rekening, String bank) throws RemoteException;
    
    /**
     * onregistreer een rekening bij de centrale bank, de rekening is hierna niet meer bekend
     * @param rekening het rekeing nummer van de rekening om weg te halen
     * @param bank de bank waar de rekening bij geregistreerd is
     * @return of het weghalen gelukt is
     * @exception RemoteException als er een RMI fout optreed
     */
    public boolean onregistreerRekening(int rekening, String bank) throws RemoteException;
    
    /**
     * registreer een bank bij de centrale bank, hij moet IBankCentraleBank implementeren
     * @param naam de naam van de bank
     * @param bank de bank om te registreren
     * @return of de bank toegevoegd kan worden, niet als de bank al bestaat
     * @exception RemoteException als er een RMI fout optreed
     */
    public boolean registreerBank(String naam, IBankCentraleBank bank) throws RemoteException;
    
    /**
     * onregistreer een bank bij de centrale bank
     * @param naam de naam van de bank
     * @param bank de bank om te onregistreren
     * @return of de bank verwijderd kan worden
     * @exception RemoteException als er een RMI fout optreed
     */
    public boolean onregistreerBank(String naam, IBankCentraleBank bank) throws RemoteException;
}
