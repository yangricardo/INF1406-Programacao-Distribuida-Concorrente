package contracts;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProdutorInterface extends Remote {
	public ConjuntoMatrizes obtemMatrizes() throws RemoteException, FileNotFoundException;
}
