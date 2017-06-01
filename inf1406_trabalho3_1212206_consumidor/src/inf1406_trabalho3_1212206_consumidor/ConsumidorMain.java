package inf1406_trabalho3_1212206_consumidor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import contracts.Callback;
import contracts.Configuracao;
import contracts.ConjuntoMatrizes;
import contracts.Execucao;
import contracts.Matrix;
import contracts.Produtor;

public class ConsumidorMain {

	private static Registry registry = null;
	private static Configuracao configStub = null;
	private static Execucao execucaoStub = null;
	private static Produtor produtorStub = null;

	private static void exportConfig(int portConsumidorConfig, String webServiceConfig) throws RemoteException {

		// Gera objeto remoto a ser exportado
		ConfiguracaoImpl config = new ConfiguracaoImpl();
		configStub = null;
		try {
			configStub = (Configuracao) UnicastRemoteObject.exportObject(config, 0);
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
				throw new RemoteException("Erro ao receber referencia para o RMIRegistry na porta " + portConsumidorConfig,e1);
			}
		}

		// Registra o stub de configuracação nomeado como webServiceConfig
		try {
			registry.rebind(webServiceConfig, configStub);
			System.out.println(webServiceConfig + " está ativado na porta " + portConsumidorConfig + " disponivel!");
		} catch (AccessException e) {
			throw new AccessException("Erro de permissão para executar ação de rebind para " + webServiceConfig
					+ " no RMIRegistry em " + portConsumidorConfig, e);
		} catch (RemoteException e) {
			throw new RemoteException(
					"Erro ao exportar stub de " + webServiceConfig + " no RMIRegistry em " + portConsumidorConfig, e);
		}
	}

	private static void getServidorExecucao(String hostExecucao, int portExecucao) throws NotBoundException, RemoteException {
		// ***Obtenção do objeto Remoto do servidor de execucao
		try {
			registry = LocateRegistry.getRegistry(hostExecucao, portExecucao);
		} catch (RemoteException e) {
			throw new RemoteException("RMIRegistry de "+hostExecucao+":"+portExecucao+" indisponivel", e);
		}

		//recebe referencia para o objeto remoto do servidor de execução
		try {
			execucaoStub = (Execucao) registry.lookup("Execucao");
		} catch (AccessException e) {
			throw new AccessException("Erro de permissão", e);
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao obter objeto remoto", e);
		} catch (NotBoundException e) {
			throw new NotBoundException("Objeto não existente no RMIRegistry de "+hostExecucao+":"+portExecucao+"!");
		}
	}
	
	private static void getProdutor(String hostProdutor, int portProdutor) throws RemoteException, NotBoundException{
		//Recuperamos o registro do produtor
		try {
			registry = LocateRegistry.getRegistry(hostProdutor, portProdutor);
		} catch (RemoteException e) {
			throw new RemoteException("RMIRegistry de "+hostProdutor+":"+portProdutor+" indisponivel", e);
		}
		//Recuperamos o objeto remoto específico do registro do Produto
		try {
			produtorStub = (Produtor) registry.lookup("Produtor");
		} catch (AccessException e) {
			throw new AccessException("Erro de permissão", e);
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao obter objeto remoto", e);
		} catch (NotBoundException e) {
			throw new NotBoundException("Objeto não existente no RMIRegistry de "+hostProdutor+":"+portProdutor+"!");
		}		
	}
	
	public static void main(String[] args) {

		Path policy = Paths.get(System.getProperty("user.dir"), "consumidor.policy");
		Path codeBase = Paths.get(System.getProperty("java.class.path"));
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

		int portConsumidor = 1100;
		String hostProdutor = args[0];
		int portProdutor = Integer.parseInt(args[1]);
		String hostExecucao = args[2];
		int portExecucao = Integer.parseInt(args[3]);

		try {
			exportConfig(portConsumidor, "ConfiguracaoConsumidor");
			getProdutor(hostProdutor, portProdutor);
			getServidorExecucao(hostExecucao, portExecucao);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
			
		//Obtendo um conjunto de matrizes do produtor
		ConjuntoMatrizes conjunto = null;
		try {
			conjunto = produtorStub.obtemMatrizes();
			
			int dim = conjunto.getDimMatrices();
			Matrix matrix1 = conjunto.getMatrices().get(0);
			Matrix matrix2 = conjunto.getMatrices().get(1);
			matrix1.print();
			matrix2.print();
			System.out.println(dim);
			for(int i = 0; i < dim; i++) {
				for(int j = 0; j < dim; j++) {
					Callback callbackTask = new CallbackImpl();
					ScalarProduct task = new ScalarProduct(i, j, dim, matrix1, matrix2,callbackTask);
					try {
						execucaoStub.execute(task);
						System.out.println("Servidor executou! (?)");
					} catch (RemoteException e){
						System.out.println("Servidor de Execução indiponível");
						e.printStackTrace();
					}
				}
			}
		} catch (RemoteException e) {
			System.err.print("Erro ao obter matrizes do produtor:\n"+e);
			System.exit(1);
		}
		
	}

}
