package example.hello;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InHello extends Remote {
	String sayHello() throws RemoteException;

	String sayHello(String name) throws RemoteException;
}
