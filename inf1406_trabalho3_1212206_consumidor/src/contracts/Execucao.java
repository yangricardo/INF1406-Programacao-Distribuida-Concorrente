package contracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Execucao extends Remote{
	public void setNumThreads(int numThreads) throws RemoteException;
	public <T> void execute(Tarefa<T> tarefa) throws RemoteException;
	public void shutdown() throws RemoteException;	
}
