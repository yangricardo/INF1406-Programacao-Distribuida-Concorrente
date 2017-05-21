package example.hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server{

	public Server() {
	}


	public static void main(String[] args) {
		
		System.setProperty("java.security.policy", "file:C:///Users//Yang//Desktop//INF1406//INF1406-Programacao-Distribuida-Concorrente//RMIHelloServer//bin//");
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			//Gera objeto remoto que implementa a interface
			Hello obj = new Hello();
			InHello stub = (InHello) UnicastRemoteObject.exportObject(obj, 12000);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry(12000);
			registry.rebind("Hello", stub);
			
			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
