package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import contracts.Callback;
import contracts.Configuracao;
import contracts.ConjuntoMatrizes;
import contracts.Execucao;
import contracts.Matrix;
import contracts.Produtor;
import contracts.Resultado;

public class ConsumidorMain {

	private static Registry registry = null;
	private static Configuracao configStub = null;
	private static Execucao execucaoStub = null;
	private static Produtor produtorStub = null;
	private static String tableFormat = "%30s%30s%30f\n";

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

		// Registra o stub de configuraca��o nomeado como webServiceConfig
		try {
			registry.rebind(webServiceConfig, configStub);
			System.out.println(webServiceConfig + " est� ativado na porta " + portConsumidorConfig + " disponivel!");
		} catch (AccessException e) {
			throw new AccessException("Erro de permiss�o para executar a��o de rebind para " + webServiceConfig
					+ " no RMIRegistry em " + portConsumidorConfig, e);
		} catch (RemoteException e) {
			throw new RemoteException(
					"Erro ao exportar stub de " + webServiceConfig + " no RMIRegistry em " + portConsumidorConfig, e);
		}
	}

	private static void getServidorExecucao(String hostExecucao, int portExecucao, String servidorExecucao) throws NotBoundException, RemoteException {
		// ***Obten��o do objeto Remoto do servidor de execucao
		try {
			registry = LocateRegistry.getRegistry(hostExecucao, portExecucao);
		} catch (RemoteException e) {
			throw new RemoteException("RMIRegistry de "+hostExecucao+":"+portExecucao+" indisponivel", e);
		}

		//recebe referencia para o objeto remoto do servidor de execu��o
		try {
			execucaoStub = (Execucao) registry.lookup("Execucao");
		} catch (AccessException e) {
			throw new AccessException("Erro de permiss�o ao obter objeto remoto Servidor Execu��o", e);
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao obter objeto remoto Servidor Execu��o", e);
		} catch (NotBoundException e) {
			throw new NotBoundException("Objeto Servidor Execu��o n�o existente no RMIRegistry de "+hostExecucao+":"+portExecucao+"!");
		}
	}
	
	private static void getProdutor(String hostProdutor, int portProdutor, String produtor) throws RemoteException, NotBoundException{
		//Recuperamos o registro do produtor
		try {
			registry = LocateRegistry.getRegistry(hostProdutor, portProdutor);
		} catch (RemoteException e) {
			throw new RemoteException("RMIRegistry de "+hostProdutor+":"+portProdutor+" indisponivel", e);
		}
		//Recuperamos o objeto remoto espec�fico do registro do Produto
		try {
			produtorStub = (Produtor) registry.lookup("Produtor");
		} catch (AccessException e) {
			throw new AccessException("Erro de permiss�o ao obter objeto remoto Produtor", e);
		} catch (RemoteException e) {
			throw new RemoteException("Erro ao obter objeto remoto Produtor", e);
		} catch (NotBoundException e) {
			throw new NotBoundException("Objeto Produtor n�o existente no RMIRegistry de "+hostProdutor+":"+portProdutor+"!");
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
			throw new IllegalArgumentException("A ordem dos argumentos requeridos s�o: \n"
					+ "\\Host_Produtor \\Port_Produtor " + "\\Host_Execucao \\Port_Execucao ");
		}

		int portConsumidor = 1100;
		String hostProdutor = args[0];
		int portProdutor = Integer.parseInt(args[1]);
		String hostExecucao = args[2];
		int portExecucao = Integer.parseInt(args[3]);

		try {
			exportConfig(portConsumidor, "ConfiguracaoConsumidor");
			getProdutor(hostProdutor, portProdutor,"Produtor");
			getServidorExecucao(hostExecucao, portExecucao,"Execucao");
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
			
		//Obtendo um conjunto de matrizes do produtor
		ConjuntoMatrizes conjunto = null;
		try {
			conjunto = produtorStub.obtemMatrizes();
			int dim = conjunto.getDimMatrices();
			int matrixIndex = 0;
			MatrixImpl resultadoFinal = new MatrixImpl();
			for(Matrix result : conjunto.getMatrices()) {
				result = (MatrixImpl)result;
				//Obtemos a matriz a ser multiplicada
				Matrix multiplied = conjunto.getMatrices().get(matrixIndex);
				HashMap<Callback, Semaphore> tasks = new HashMap<Callback, Semaphore>();
				for(int i = 0; i < dim; i++) {
					for(int j = 0; j < dim; j++) {
						Resultado resultado = new ResultadoImpl(i, j, 0.0);
						Semaphore sema = new Semaphore(0);
						Callback callbackTask = new CallbackImpl(resultado, sema);
						Callback callbackStub = (Callback) UnicastRemoteObject.exportObject(callbackTask, 0);
						ScalarProduct task = new ScalarProduct(i, j, dim, result, multiplied,callbackStub);
						try {
							execucaoStub.execute((Runnable)task);
							tasks.put(callbackTask, sema);
							
						} catch (RemoteException e){
							System.out.println("Servidor de Execu��o indipon�vel");
							e.printStackTrace();
						}
					}
				}				
				Iterator<Callback> callbacks = tasks.keySet().iterator();
				System.out.println("\n-----------------------------");
				System.out.format("Execu��o das tarefas - Matrizes %d & %d\n", matrixIndex, matrixIndex + 1);
				System.out.println("-----------------------------");
				System.out.format("%30s%30s%30s\n", "Timestamp", "C�lula", "Resultado");
				
				while(callbacks.hasNext()) {
					Callback c = callbacks.next();
					tasks.get(c).acquire();
					
					Resultado resultado = c.getResultado();
					String celula = "["+resultado.getLine()+","+resultado.getColumn()+"]";
					System.out.format(ConsumidorMain.tableFormat, new Timestamp(System.currentTimeMillis()), celula, resultado.getResultado());
					result.setValue(resultado.getLine(), resultado.getColumn(), resultado.getResultado());
					callbacks.remove();
				}
				matrixIndex++;
				resultadoFinal = (MatrixImpl)result;
			//END for
			}
			System.out.println("\n-----------------------------");
			System.out.println("Resultado Final");
			System.out.println("-----------------------------");
			resultadoFinal.print();
		} catch (RemoteException e) {
			System.err.print("Erro ao obter matrizes do produtor:\n"+e);
			System.exit(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
