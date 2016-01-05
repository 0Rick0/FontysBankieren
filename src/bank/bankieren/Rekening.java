package bank.bankieren;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class Rekening implements IRekeningTbvBank {

    private static final long serialVersionUID = 7221569686169173632L;
    private static final int KREDIETLIMIET = -10000;
    private int nr;
    private IKlant eigenaar;
    private Money saldo;
    private transient List<IRekeningUpdateListener> listeners;

    /**
     * creatie van een bankrekening met saldo van 0.0<br>
     * de constructor heeft package-access omdat de PersistentAccount-objecten door een
     * het PersistentBank-object worden beheerd
     * @see banking.persistence.PersistentBank
     * @param number het bankrekeningnummer
     * @param klant de eigenaar van deze rekening
     * @param currency de munteenheid waarin het saldo is uitgedrukt
     */
    Rekening(int number, IKlant klant, String currency) {
        this(number, klant, new Money(0, currency));
    }

    /**
     * creatie van een bankrekening met saldo saldo<br>
     * de constructor heeft package-access omdat de PersistentAccount-objecten door een
     * het PersistentBank-object worden beheerd
     * @see banking.persistence.PersistentBank
     * @param number het bankrekeningnummer
     * @param name de naam van de eigenaar
     * @param city de woonplaats van de eigenaar
     * @param currency de munteenheid waarin het saldo is uitgedrukt
     */
    Rekening(int number, IKlant klant, Money saldo) {
        this.nr = number;
        this.eigenaar = klant;
        this.saldo = saldo;
        listeners = new ArrayList<>();
    }

    public boolean equals(Object obj) {
        return nr == ((IRekening) obj).getNr();
    }

    public int getNr() {
        return nr;
    }

    public String toString() {
        return nr + ": " + eigenaar.toString();
    }

    boolean isTransferPossible(Money bedrag) {
        return (bedrag.getCents() + saldo.getCents() >= KREDIETLIMIET);
    }

    public IKlant getEigenaar() {
        return eigenaar;
    }

    public Money getSaldo() {
        return saldo;
    }

    public boolean muteer(Money bedrag) {
        if (bedrag.getCents() == 0) {
            throw new RuntimeException(" bedrag = 0 bij aanroep 'muteer'");
        }

        if (isTransferPossible(bedrag)) {
            
            saldo = Money.sum(saldo, bedrag);
            listeners.forEach((l)->{
                try {
                    l.updateBalance(saldo);
                } catch (RemoteException ex) {
                    Logger.getLogger(Rekening.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public int getKredietLimietInCenten() {
        return KREDIETLIMIET;
    }

    @Override
    public void addListener(IRekeningUpdateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(IRekeningUpdateListener listener) {
        listeners.remove(listener);
    }
}
