package contracts;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Tarefa<T> extends Runnable, Serializable {
	
	public void execute() throws RemoteException;
}
