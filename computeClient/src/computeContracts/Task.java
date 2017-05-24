package computeContracts;

/**
 * 
 * @author Yang
 *	The Task interface defines a single method, 
 *	execute, which has no parameters and throws 
 *	no exceptions. Because the interface does 
 *	not extend Remote, the method in this interface 
 *	doesn't need to list java.rmi.RemoteException in
 *	its throws clause.
 *	The Task interface has a type parameter, T, which 
 *	represents the result type of the task's computation. 
 *	This interface's execute method returns the result of 
 *	the computation and thus its return type is T.
 * @param <T>
 */
public interface Task<T> {
	T execute();
}
