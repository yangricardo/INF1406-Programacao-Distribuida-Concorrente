package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import contracts.Configuracao;
import contracts.Execucao;
import contracts.Produtor;

public class ConsumidorMain {

	private static final Path policy = Paths.get(System.getProperty("user.dir"), "consumidor.policy");
	private static final Path codeBase = Paths.get(System.getProperty("java.class.path"));
	private static final int portConsumidorConfig = 1100;
	private static final String remoteConfigService = "ConfiguracaoConsumidor";
	private static String hostProdutor;
	private static int portProdutor;
	private static String hostExecucao;
	private static int portExecucao;

	private static Registry registry = null;
	private static Produtor produtorStub = null;
	private static Execucao execucaoStub = null;
	private static ConsumidorImpl consumidor = null;
	private static Configuracao configStub = null;

	private static void getProdutor(String produtor) throws RemoteException, NotBoundException {
		// Recuperamos o registro do produtor
		try {
			registry = LocateRegistry.getRegistry(hostProdutor, portProdutor);
		} catch (RemoteException e) {
			throw new RemoteException("RMIRegistry de " + hostProdutor + ":" + portProdutor + " indisponivel", e);
		}
		// Recuperamos o objeto remoto específico do registro do Produto
		try {
			produtorStub = (Produtor) registry.lookup(produtor);
		} catch (AccessException e) {
			throw new AccessException("Erro de permissão ao obter objeto remoto " + produtor, e);
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao obter objeto remoto " + produtor, e);
		} catch (NotBoundException e) {
			throw new NotBoundException("Objeto  " + produtor + " não existente no RMIRegistry de " + hostProdutor + ":"
					+ portProdutor + "!");
		}
	}

	private static void getServidorExecucao(String servidorExecucao) throws NotBoundException, RemoteException {
		// ***Obtenção do objeto Remoto do servidor de execucao
		try {
			registry = LocateRegistry.getRegistry(hostExecucao, portExecucao);
		} catch (RemoteException e) {
			throw new RemoteException("RMIRegistry de " + hostExecucao + ":" + portExecucao + " indisponivel", e);
		}

		// recebe referencia para o objeto remoto do servidor de execução
		try {
			execucaoStub = (Execucao) registry.lookup(servidorExecucao);
		} catch (AccessException e) {
			throw new AccessException("Erro de permissão ao obter objeto remoto " + servidorExecucao, e);
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao obter objeto remoto " + servidorExecucao, e);
		} catch (NotBoundException e) {
			throw new NotBoundException("Objeto " + servidorExecucao + " não existente no RMIRegistry de "
					+ hostExecucao + ":" + portExecucao + "!");
		}
	}

	private static void exportConfig() throws RemoteException {
		try {
			configStub = consumidor.exportConfig();
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao gerar objeto remoto de Configuracao", e);
		}
		// Obtem referencia ao RMIRegistry
		try {
			registry = LocateRegistry.createRegistry(portConsumidorConfig);
		} catch (RemoteException e) {
			try {
				registry = LocateRegistry.getRegistry(portConsumidorConfig);
			} catch (RemoteException e1) {
				throw new RemoteException(
						"Erro ao receber referencia para o RMIRegistry na porta " + portConsumidorConfig, e1);
			}
		}

		// Registra o stub de configuracação nomeado como webServiceConfig
		try {
			registry.rebind(remoteConfigService, configStub);
			System.out.println(remoteConfigService + " está ativado na porta " + portConsumidorConfig + " disponivel!");
		} catch (AccessException e) {
			throw new AccessException("Erro de permissão para executar ação de rebind para " + remoteConfigService
					+ " no RMIRegistry em " + portConsumidorConfig, e);
		} catch (RemoteException e) {
			throw new RemoteException(
					"Erro ao exportar stub de " + remoteConfigService + " no RMIRegistry em " + portConsumidorConfig, e);
		}
	}

	public static void main(String[] args) {
		System.setProperty("java.security.policy", policy.toUri().toString());
		System.setProperty("java.rmi.server.codebase", codeBase.toUri().toString());
		System.setProperty("java.rmi.server.useCodebaseOnly", "false");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		if (args.length != 4) {
			throw new IllegalArgumentException("A ordem dos argumentos requeridos são: \n"
					+ "\\Host_Produtor \\Port_Produtor " + "\\Host_Execucao \\Port_Execucao ");
		}

		hostProdutor = args[0];
		portProdutor = Integer.parseInt(args[1]);
		hostExecucao = args[2];
		portExecucao = Integer.parseInt(args[3]);

		try {
			getProdutor("Produtor");
			getServidorExecucao("Execucao");
			consumidor = new ConsumidorImpl(produtorStub, execucaoStub);
			exportConfig();
		} catch (RemoteException | NotBoundException e) {
			System.err.println(e);
			System.exit(1);
		}
		
		consumidor.runConsumidor();
		
	}
}
