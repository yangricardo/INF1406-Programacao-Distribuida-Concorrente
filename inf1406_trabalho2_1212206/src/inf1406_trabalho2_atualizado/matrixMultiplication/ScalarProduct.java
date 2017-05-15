package inf1406_trabalho2_atualizado.matrixMultiplication;

import java.util.concurrent.Callable;

import inf1406_trabalho2_atualizado.model.Matrix;

public class ScalarProduct implements Callable<Double>{

	private int line;
	private int column;
	private int matricesDimension;
	private Matrix matrix1, matrix2;
	
	public ScalarProduct(int line, int column,int matricesDimension,Matrix matrix1, Matrix matrix2) {
		this.line = line;
		this.column = column;
		this.matricesDimension = matricesDimension;
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
	}

	@Override
	public Double call() throws Exception {
		Double resultCell = 0.0;
		for(int i = 0; i < matricesDimension; i++){
			resultCell += matrix1.getMatrixCell(line, i)*matrix2.getMatrixCell(i, column);
		}
		return resultCell;
	}
}
