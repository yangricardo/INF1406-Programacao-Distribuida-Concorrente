package contracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Configuracao extends Remote {
	void aplicaIntervalo(int intervalo) throws 	RemoteException;
}
