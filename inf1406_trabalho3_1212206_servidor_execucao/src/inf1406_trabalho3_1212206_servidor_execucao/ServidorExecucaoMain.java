package inf1406_trabalho3_1212206_servidor_execucao;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import contracts.Execucao;

public class ServidorExecucaoMain {

	public static void main(String[] args) throws RemoteException,AccessException {
		
		if(args.length != 1)
			System.exit(1);
		
		int numThreads = Integer.parseInt(args[0]);
		String host = "localhost";
		int port = 1099;
		String webService = "Execucao";
		Path policy = Paths.get(System.getProperty("user.dir"), "servidorexecucao.policy");
		Path codeBase = Paths.get(System.getProperty("java.class.path"));

		System.setProperty("java.security.policy", policy.toUri().toString());
		System.setProperty("java.rmi.server.codebase", codeBase.toUri().toString());
		System.setProperty("java.rmi.server.useCodebaseOnly", "false");

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		ServidorExecucaoImpl servidorExecucao = new ServidorExecucaoImpl(numThreads);
		Execucao servidorExecucaoStub = null;
		try {
			servidorExecucaoStub = (Execucao) UnicastRemoteObject.exportObject(servidorExecucao, 0);
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao gerar objeto remoto do Servidor de Execucao",e);
		}

		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			try {
				registry = LocateRegistry.getRegistry(port);
			} catch (RemoteException e1) {
				throw new RemoteException("Erro ao receber referencia para o RMIRegistry na porta " + port, e1);
			}
		}

		try {
			registry.rebind(webService, servidorExecucaoStub);
			System.out.println(webService + " está ativado na porta " + port + " com "+ numThreads + " threads disponiveis!");
		} catch (AccessException e) {
			throw new AccessException("Erro de permissão para executar ação de rebind para " + webService 
					+ " no RMIRegistry em " + port + ":" + host, e);
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao exportar stub de " + webService + " no RMIRegistry em " + port + ":" + host, e);
		}		
	}

}
