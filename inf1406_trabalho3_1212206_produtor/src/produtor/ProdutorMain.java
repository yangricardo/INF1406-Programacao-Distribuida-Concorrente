package produtor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import contracts.Produtor;

public class ProdutorMain {
	
	public static void main(String[] args) {
		String host = "localhost";
		int port = 1101;
		String webService = "Produtor";
		Path policy = Paths.get(System.getProperty("user.dir"), "produtor.policy");
		Path codeBase = Paths.get(System.getProperty("java.class.path"));

		System.setProperty("java.security.policy", policy.toUri().toString());
		System.setProperty("java.rmi.server.codebase", codeBase.toUri().toString());
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		if(args.length != 1){
			System.err.println("Coloque o nome/caminho do arquivo de matrizes");
			System.exit(1);
		}
		
		ProdutorImpl produtor = new ProdutorImpl(args);
		Produtor stub = null;
		try {
			stub = (Produtor) UnicastRemoteObject.exportObject(produtor, 0);				
		}
		catch (RemoteException e) {
			System.err.println("Erro ao gerar objeto remoto");
			System.exit(1);
		}

		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			try {
				registry = LocateRegistry.getRegistry(port);
			} catch (RemoteException e1) {
				System.err.println("Erro ao receber referencia para o RMIRegistry na porta " + port);
				System.exit(1);
			}
		}
		try {
			registry.rebind(webService, stub);
			System.out.println(webService + " está ativado na porta " + port + "!");
		} catch (AccessException e) {
			System.err.println("Erro de permissão para executar ação de rebind para " + webService
					+ " no RMIRegistry em " + port + ":" + host);
			System.exit(1);
		} catch (RemoteException e) {
			System.err.println("Erro ao exportar stub de " + webService + " no RMIRegistry em " + port + ":" + host);
			System.exit(1);
		}		
	}
}
