package bank.bankieren;

import java.io.Serializable;

public interface IRekening extends Serializable {
  int getNr();
  Money getSaldo();
  IKlant getEigenaar();
  int getKredietLimietInCenten();
  void addListener(IRekeningUpdateListener listener);
  void removeListener(IRekeningUpdateListener listener);
}

