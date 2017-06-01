package produtor;

import java.util.ArrayList;

import contracts.Matrix;

public class MatrixImpl implements Matrix {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ArrayList<Double>> matrix;

	public MatrixImpl() {
		super();
		matrix = new ArrayList<ArrayList<Double>>();
	}
	
	public MatrixImpl(ArrayList<ArrayList<Double>> matrix) {
		super();
		this.matrix = matrix;
	}
	
	public void setDimension(int dimension){
		for(int i = 0; i < dimension; i++){
			ArrayList<Double> line = new ArrayList<Double>();
			for(int j = 0; j < dimension; j++){
				line.add(null);
			}	
			matrix.add(line);
		}
	}

	public void setValue(int line, int column, Double value){
		matrix.get(line).set(column, value);
	}
	
	public ArrayList<ArrayList<Double>> getMatrix() {
		return matrix;
	}
	
	public Double getMatrixCell(int line, int column){
		return matrix.get(line).get(column);
	}
	
    public void addToInnerArray(int index, Double value) {
        while (index >= this.matrix.size()) {
            this.matrix.add(new ArrayList<Double>());
        }
        this.matrix.get(index).add(value);
    }

    public void addToInnerArray(int line, int column, Double value) {
        while (line >= this.matrix.size()) {
            this.matrix.add(new ArrayList<Double>());
        }

        ArrayList<Double> inner = this.matrix.get(line);
        while (column >= inner.size()) {
            inner.add(null);
        }

        inner.set(column, value);
    }
		
	@Override
	public String toString() {
		StringBuilder sMatrix = new StringBuilder();
		for(ArrayList<Double> line : matrix){
			for(Double celColumn : line){
				sMatrix.append(celColumn.toString() + " ");
			}
			sMatrix.append("\n");
		}
		return sMatrix.toString();
	}

	public void print(){
		System.out.println(this.toString());
	}	
}
