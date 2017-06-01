package contracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Produtor extends Remote {
	public ConjuntoMatrizes obtemMatrizes() throws RemoteException;
}
