package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Matrix implements Serializable {
	
	private ArrayList<ArrayList<Double>> matrix;

	public Matrix() {
		super();
		matrix = new ArrayList<ArrayList<Double>>();
	}
	
	public Matrix(ArrayList<ArrayList<Double>> matrix) {
		super();
		this.matrix = matrix;
	}

	public ArrayList<ArrayList<Double>> getMatrix() {
		return matrix;
	}
	
	public Double getMatrixCell(int line, int column){
		return matrix.get(line).get(column);
	}

	public void setMatrix(ArrayList<ArrayList<Double>> matrix) {
		this.matrix = matrix;
	}
	
	public void setMatrixCell(int line, int column, Double value){
		this.matrix.get(line).set(column, value);
	}
	
	public void addNewLine(Double value){
		this.matrix.add(new ArrayList<Double>());
		addValue(value);
	}
	public void addValue(Double value){
		this.matrix.get(this.matrix.size()-1).add(value);
	}
	
    public void addToInnerArray(int index, Double value) {
        while (index >= this.matrix.size()) {
            this.matrix.add(new ArrayList<Double>());
        }
        this.matrix.get(index).add(value);
    }

    public void addToInnerArray(int index, int index2, Double value) {
        while (index >= this.matrix.size()) {
            this.matrix.add(new ArrayList<Double>());
        }

        ArrayList<Double> inner = this.matrix.get(index);
        while (index2 >= inner.size()) {
            inner.add(null);
        }

        inner.set(index2, value);
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
