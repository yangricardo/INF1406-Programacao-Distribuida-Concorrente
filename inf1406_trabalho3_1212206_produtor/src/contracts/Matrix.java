package contracts;

import java.io.Serializable;
import java.util.ArrayList;

public interface Matrix extends Serializable {

	public ArrayList<ArrayList<Double>> getMatrix();
	
	public void setDimension(int dimension);
	
	public void setValue(int line, int column, Double value);

	public Double getMatrixCell(int line, int column);

	public String toString();
	
	public void print();
}
