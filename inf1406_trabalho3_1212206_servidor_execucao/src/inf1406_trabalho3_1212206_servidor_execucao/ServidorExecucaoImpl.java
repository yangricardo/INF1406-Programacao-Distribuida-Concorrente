package inf1406_trabalho3_1212206_servidor_execucao;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import contracts.Execucao;

public class ServidorExecucaoImpl implements Execucao {

	private int numThreads;
	private ExecutorService pool;
	
	protected ServidorExecucaoImpl() throws RemoteException {
		super();
		this.numThreads = 1;
		this.pool = Executors.newFixedThreadPool(numThreads);
	}
	
	public ServidorExecucaoImpl(int numThreads) throws RemoteException {
		super();
		this.numThreads = numThreads;
		this.pool = Executors.newFixedThreadPool(numThreads);
	}

	@Override
	public void setNumThreads(int numThreads) throws RemoteException {
		this.numThreads = numThreads;
		this.pool = Executors.newFixedThreadPool(numThreads);		
	}

	@Override
	public void shutdown() throws RemoteException {
		pool.shutdown();
		try {
			pool.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RemoteException("Erro de Interrupção durante a execução da tarefa", e);
		}
	}

	@Override
	public void execute(Runnable tarefa) throws RemoteException {
		this.pool.submit(tarefa);		
	}	
	
}
