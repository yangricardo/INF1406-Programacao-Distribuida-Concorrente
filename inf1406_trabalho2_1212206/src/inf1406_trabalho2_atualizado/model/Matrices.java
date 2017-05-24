package inf1406_trabalho2_atualizado.model;

import java.util.ArrayList;

public class Matrices {
	int dimMatrices;
	int numMatrices;
	ArrayList<Matrix> matrices;
	
	public Matrices() {
		super();
		matrices = new ArrayList<Matrix>();
	}

	public Matrices(ArrayList<Matrix> matrices) {
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
