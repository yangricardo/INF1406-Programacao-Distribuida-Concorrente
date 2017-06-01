package main;

import java.util.ArrayList;

import contracts.Matrix;

public class MatrixImpl implements Matrix {

	private static final long serialVersionUID = 2L;
	
	private ArrayList<ArrayList<Double>> matrix;

	public MatrixImpl() {
		super();
		matrix = new ArrayList<ArrayList<Double>>();
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
