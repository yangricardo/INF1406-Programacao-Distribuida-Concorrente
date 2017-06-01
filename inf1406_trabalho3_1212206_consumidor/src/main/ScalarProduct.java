package main;

import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

import contracts.Callback;
import contracts.Matrix;
import contracts.Resultado;
import contracts.Tarefa;

public class ScalarProduct implements Tarefa {

	private static final long serialVersionUID = 1L;
	
	private int line;
	private int column;
	private int matricesDimension;
	private Matrix matrix1, matrix2;
	private Double resultCell;
	private Callback callback;
	private Semaphore sema;
	
	public ScalarProduct(int line, int column,int matricesDimension,Matrix matrix1, Matrix matrix2, Callback callback, Semaphore sema) {
		this.line = line;
		this.column = column;
		this.matricesDimension = matricesDimension;
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.callback = callback;
		this.sema = sema;
	}
	
	public void print() {
		System.out.println(this.resultCell);
	}

	@Override
	public void run() {
		try {
			this.execute();
			System.out.println(resultCell);
			System.out.println("Releasing!");
			Resultado resultado = callback.getResultado();
			
			resultado.setResultado(resultCell);
			callback.entregaResultado(resultado);	
		} catch (RemoteException e) {
			try {
				this.callback.entregaResultado(null);
				System.err.println("Erro ao calcular M["+this.line+","+this.column+"]:\n"+e);
			} catch (RemoteException e1) {
				System.err.println("Erro de acesso remoto ao objeto callback passado:\n"+e1);
			}
		}	
	}

	@Override
	public void execute() throws RemoteException {
		resultCell = 0.0;
		for(int i = 0; i < matricesDimension; i++){
			resultCell += matrix1.getMatrixCell(line, i)*matrix2.getMatrixCell(i, column);
		}	
	}
}
