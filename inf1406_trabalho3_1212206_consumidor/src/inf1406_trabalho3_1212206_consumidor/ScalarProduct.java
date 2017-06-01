package inf1406_trabalho3_1212206_consumidor;

import java.rmi.RemoteException;

import contracts.Callback;
import contracts.Matrix;
//import contracts.Matrix;
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
	
	public ScalarProduct(int line, int column,int matricesDimension,Matrix matrix1, Matrix matrix2, Callback callback) {
		this.line = line;
		this.column = column;
		this.matricesDimension = matricesDimension;
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.callback = callback;
	}
	
	public void print() {
		System.out.println(this.resultCell);
	}

	@Override
	public void run() {
		try {
			this.execute();
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
		Resultado resultado = new ResultadoImpl(line, column, resultCell);
		callback.entregaResultado(resultado);
	}
}
