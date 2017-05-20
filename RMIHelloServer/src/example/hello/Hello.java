package example.hello;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Hello extends UnicastRemoteObject implements InHello{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Hello() throws RemoteException {
		super();
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}
	
	@Override
	public String sayHello(String name) throws RemoteException {
		return "Hello, "+name+"!";
	}

}
