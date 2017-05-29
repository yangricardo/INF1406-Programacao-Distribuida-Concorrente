package inf1406_trabalho3_1212206_produtor;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import inf1406_trabalho3_1212206_produtor.model.ConjuntoMatrizes;

public interface ProdutorInterface extends Remote {
	public ConjuntoMatrizes obtemMatrizes() throws RemoteException, FileNotFoundException;
}
