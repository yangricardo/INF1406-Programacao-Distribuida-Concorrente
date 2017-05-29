import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AdditionServer {

	public static void main(String[] args) {

		System.setProperty("java.security.policy", "file:./server.policy");
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");
		
		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
			
		try {
			Addition hello = new Addition();
			AdditionInterface stub = (AdditionInterface) UnicastRemoteObject.exportObject(hello, 0);
			Registry registry = LocateRegistry.createRegistry(1100);
			registry.rebind("Add", stub);
			
			System.out.println("AdditionServer está pronto!");
		} catch (RemoteException e) {
			System.err.println("Inicialização do AdditionServer falhou - Erro de acesso remoto\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		}
		
	}
}
