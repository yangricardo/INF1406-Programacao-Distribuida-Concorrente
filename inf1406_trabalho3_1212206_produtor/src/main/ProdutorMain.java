package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import contracts.Configuracao;
import contracts.Produtor;

public class ProdutorMain {
	
	private static String host = "localhost";
	private static int port = 1101;
	private static String webService = "Produtor";
	private static int configPort = 1102;
	private static String configWebService = "ConfiguracaoProdutor";
	private static Path policy = Paths.get(System.getProperty("user.dir"), "produtor.policy");
	private static Path codeBase = Paths.get(System.getProperty("java.class.path"));
	private static ProdutorImpl produtor;
	
	
	public static void initProdutor(String filenameMatrices){
		produtor = new ProdutorImpl(filenameMatrices);
		initConfigurador();
		
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
	
	public static void initConfigurador(){
		Configuracao configStub = null;
		try {
			configStub = produtor.exportConfig();
		} catch (RemoteException e) {
			System.err.println("Erro ao gerar objeto remoto de configuracao");
			System.exit(1);
		}
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(configPort);
		} catch (RemoteException e) {
			try {
				registry = LocateRegistry.getRegistry(configPort);
			} catch (RemoteException e1) {
				System.err.println("Erro ao receber referencia para o RMIRegistry na porta " + configPort);
				System.exit(1);
			}
		}
		try {
			registry.rebind(configWebService, configStub);
			System.out.println(configWebService + " está ativado na porta " + configPort + "!");
		} catch (AccessException e) {
			System.err.println("Erro de permissão para executar ação de rebind para " + configWebService
					+ " no RMIRegistry em " + configPort + ":" + host);
			System.exit(1);
		} catch (RemoteException e) {
			System.err.println("Erro ao exportar stub de " + configWebService + " no RMIRegistry em " + configPort + ":" + host);
			System.exit(1);
		}	
		
	}
	
	public static void main(String[] args) {
		
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
		
		initProdutor(args[0]);	
		produtor.runReaderThread();
			
	}
}
