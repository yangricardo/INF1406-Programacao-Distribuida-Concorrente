package example.hello;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

	public Client() {
	}

	public static void main(String[] args) {

		System.setProperty("java.security.policy", "file:C:///Users//Yang//Desktop//INF1406//RMIHelloClient//server.policy");
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new SecurityManager());
		}
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(12000);
			InHello stub = null;
			try {
				stub = (InHello) registry.lookup("Hello");
				
			} catch (AccessException e) {
				System.err.println("Permissão de Acesso Negada:\n");
				e.printStackTrace();
			} catch (RemoteException e) {
				System.err.println("Erro ao acessar Serviço 'Hello':\n");
				e.printStackTrace();
			} catch (NotBoundException e) {
				System.err.println("Serviço desconhecido pelo RMIRegistry:\n");
				e.printStackTrace();
			}
			String response = stub.sayHello();
			System.out.println(response);
		} catch (RemoteException e) {
			System.err.println("Erro ao acessar RMIRegistry:\n");
			e.printStackTrace();
		}
		
		
		
		
	}

}
