package contracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callback extends Remote {
	void entregaResultado(Resultado resultado) throws RemoteException;
}
