package inf1406_trabalho2_atualizado.matrixGenerator;

import java.util.ArrayList;
import java.util.Random;

import inf1406_trabalho2_atualizado.WriteMatricesFile.WriteMatricesFile;
import inf1406_trabalho2_atualizado.model.Matrices;
import inf1406_trabalho2_atualizado.model.Matrix;

public class MatrixGenerator {

	public static Matrix generateRandomSquareMatrix(int matrixDim) {
		ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
		Random randomGenerator = new Random();
		for (int i = 0; i < matrixDim; i++) {
			ArrayList<Double> matrixLine = new ArrayList<Double>();
			for (int j = 0; j < matrixDim; j++) {
				Double randomValue = randomGenerator.nextDouble() * randomGenerator.nextInt()
						* randomGenerator.nextInt();
				matrixLine.add(randomValue);
			}
			matrix.add(matrixLine);
		}
		return new Matrix(matrix);
	}
	
	public static Matrices generateNRandomSquareMatrix(int numMatrices, int matrixDim){
		Matrices matrices = new Matrices();
		for(int i = 0; i < numMatrices; i++){
			matrices.appendMatrix(generateRandomSquareMatrix(matrixDim));
		}
		return matrices;		
	}
	
	public static void main(String[] args) {
		WriteMatricesFile.createFile("matrices.txt");
		Matrices matrices = MatrixGenerator.generateNRandomSquareMatrix(6, 3);
		matrices.print();
		WriteMatricesFile.writeMatrices(matrices);
	}
}
