package produtor;

import java.util.ArrayList;

import contracts.ConjuntoMatrizes;
import contracts.Matrix;

public class ConjuntoMatrizesImpl implements ConjuntoMatrizes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int dimMatrices;
	int numMatrices;
	ArrayList<Matrix> matrices;
	
	public ConjuntoMatrizesImpl() {
		super();
		matrices = new ArrayList<Matrix>();
	}

	public ConjuntoMatrizesImpl(ArrayList<Matrix> matrices) {
		super();
		this.matrices = matrices;
	}
	
	public int getDimMatrices() {
		return this.dimMatrices;
	}
	
	public int getNumMatrices() {
		return this.numMatrices;
	}

	public ArrayList<Matrix> getMatrices() {
		return matrices;
	}
	
	public Matrix getMatrix(int i){
		return matrices.get(i);
	}

	public void appendMatrix(Matrix matrix){
		matrices.add(matrix);
	}
	
	public void putResultedMatrix(Matrix resultedMatrix){
		matrices.set(0, resultedMatrix);
		matrices.remove(1);
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
