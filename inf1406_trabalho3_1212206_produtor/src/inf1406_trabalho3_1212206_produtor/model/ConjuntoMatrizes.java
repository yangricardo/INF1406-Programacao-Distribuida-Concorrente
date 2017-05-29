package inf1406_trabalho3_1212206_produtor.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ConjuntoMatrizes implements Serializable {
	int dimMatrices;
	int numMatrices;
	ArrayList<Matrix> matrices;
	
	public ConjuntoMatrizes() {
		super();
		matrices = new ArrayList<Matrix>();
	}

	public ConjuntoMatrizes(ArrayList<Matrix> matrices) {
		super();
		this.matrices = matrices;
	}

	public ArrayList<Matrix> getMatrices() {
		return matrices;
	}
	
	public void setMatrices(ArrayList<Matrix> matrices) {
		this.matrices = matrices;
	}
	
	public Matrix getMatrix(int i){
		return matrices.get(i);
	}

	public void appendMatrix(Matrix matrix){
		matrices.add(matrix);
	}
	
	public void putResultedMatrix(int matrix1,int matrix2, Matrix resultedMatrix){
		matrices.set(matrix1, resultedMatrix);
		matrices.remove(matrix2);
	}
	
	public void print(){
		int i = 0;
		System.out.println("**********Matrizes Atuais");
		for(Matrix matrix : matrices){
			System.out.println("Matrix "+i+" [");
			matrix.print();
			System.out.println("]");
			i++;
		}
	}		
}