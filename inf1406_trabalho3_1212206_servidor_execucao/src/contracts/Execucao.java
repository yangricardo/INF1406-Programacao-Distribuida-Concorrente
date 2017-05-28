package contracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Execucao extends Remote{
	public void setNumThreads(int numThreads) throws RemoteException;
	public void execute(Callback<Object> tarefa) throws RemoteException;
	public void shutdown() throws RemoteException;	
}
