package contracts;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Callback extends Serializable,Runnable {
	void entregaResultado(Resultado resultado) throws RemoteException;
}
