package computeContracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * The Compute interface's executeTask method, in turn, 
 * returns the result of the execution of the Task instance 
 * passed to it. Thus, the executeTask method has its own 
 * type parameter, T, that associates its own return type
 * with the result type of the passed Task instance.
 *
 */
public interface Compute extends Remote {
	<T> T executeTask(Task<T> t) throws RemoteException;
}
