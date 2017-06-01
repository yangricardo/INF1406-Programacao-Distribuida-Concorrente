package contracts;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Tarefa extends Runnable, Serializable {
	
	public void execute() throws RemoteException;
}
