package contracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScalarProd extends Remote {
	void run() throws RemoteException;
}
