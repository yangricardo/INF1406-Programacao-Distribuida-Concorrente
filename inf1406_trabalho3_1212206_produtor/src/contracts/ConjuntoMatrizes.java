package contracts;

import java.io.Serializable;
import java.util.ArrayList;

public interface ConjuntoMatrizes extends Serializable {

	public int getDimMatrices();
	
	public int getNumMatrices();
	
	public ArrayList<Matrix> getMatrices();

	public void appendMatrix(Matrix matrix);
	
	public void putResultedMatrix(Matrix resultedMatrix);
	
	public void print();	
}
