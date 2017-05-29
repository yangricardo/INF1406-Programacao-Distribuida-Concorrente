package inf1406_trabalho3_1212206_consumidor;

import java.rmi.RemoteException;

import contracts.Callback;
import contracts.Matrix;
import contracts.Resultado;
import contracts.Tarefa;

public class ScalarProduct implements Tarefa<Resultado> {

	private static final long serialVersionUID = 1L;
	
	private int line;
	private int column;
	private int matricesDimension;
	private Matrix matrix1, matrix2;
	private Double resultCell;
	
	public ScalarProduct(int line, int column,int matricesDimension,Matrix matrix1, Matrix matrix2) {
		this.line = line;
		this.column = column;
		this.matricesDimension = matricesDimension;
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
	}

	@Override
	public void run() {
		resultCell = 0.0;
		for(int i = 0; i < matricesDimension; i++){
			resultCell += matrix1.getMatrixCell(line, i)*matrix2.getMatrixCell(i, column);
		}		
	}

	/*
	@Override
	public void entregaResultado(Resultado resultado) throws RemoteException {
		resultado.setResultCell(resultCell);		
	}
	*/
	
	public void print() {
		System.out.println(this.resultCell);
	}

	@Override
	public void execute() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
