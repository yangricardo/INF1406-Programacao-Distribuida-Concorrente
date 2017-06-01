package contracts;

import java.io.Serializable;
import java.util.ArrayList;

public interface Matrix extends Serializable {

	public ArrayList<ArrayList<Double>> getMatrix();
	
	public void setDimension(int dimension);
	
	public void setValue(int line, int column, Double value);

	public Double getMatrixCell(int line, int column);

    public void addToInnerArray(int index, Double value);

    public void addToInnerArray(int line, int column, Double value);

	public String toString();
	
	public void print();
}
