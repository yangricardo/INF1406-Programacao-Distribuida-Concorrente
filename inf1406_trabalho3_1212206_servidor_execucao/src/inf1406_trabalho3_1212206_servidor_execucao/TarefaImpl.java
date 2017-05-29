package inf1406_trabalho3_1212206_servidor_execucao;

import java.rmi.RemoteException;

import contracts.Tarefa;

public class TarefaImpl implements Tarefa<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void run() {
		try {
			this.execute();
		} catch (RemoteException e) {
			System.err.println("Erro de execucao de tarefa:\n"+ e);
		}
	}

	@Override
	public void execute() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	
}
