package main;

import java.util.Random;

import contracts.ConjuntoMatrizes;
import contracts.Matrix;



public class MatrixGenerator {

	public static Matrix generateRandomSquareMatrix(int matrixDim) {
		Matrix matrix = new MatrixImpl();
		matrix.setDimension(matrixDim);
		Random randomGenerator = new Random();
		for (int i = 0; i < matrixDim; i++) {
			for (int j = 0; j < matrixDim; j++) {
				Double randomValue = Double.valueOf(randomGenerator.nextInt(100) * randomGenerator.nextInt(100));
				matrix.setValue(i, j, randomValue);
			}
		}
		return matrix;
	}
	
	public static ConjuntoMatrizes generateNRandomSquareMatrix(int numMatrices, int matrixDim){
		ConjuntoMatrizes matrices = new ConjuntoMatrizesImpl();
		matrices.setDimMatrices(matrixDim);
		for(int i = 0; i < numMatrices; i++){
			matrices.appendMatrix(generateRandomSquareMatrix(matrixDim));
		}
		return matrices;		
	}
	
	public static void WriteMatricesFile(String fileName, int dimMatrices,int numMatrices ){
		WriteMatricesFile.createFile(fileName);
		ConjuntoMatrizes matrices = MatrixGenerator.generateNRandomSquareMatrix(numMatrices, dimMatrices);
		matrices.print();
		WriteMatricesFile.writeMatrices(matrices);
		WriteMatricesFile.openFile("matrizes.txt", true);
		WriteMatricesFile.writeNewMatricesArguments(fileName+" "+numMatrices+" "+dimMatrices+"\n");
	}
		
	public static void main(String[] args) {
		String fileName;
		int dimMatrices = 1;
		int numMatrices = 1;
		for(int i = 1; i <= 10; i++){
			fileName = "matrizesA.txt";
			fileName = fileName.replaceAll("A", Integer.toString(i));
			WriteMatricesFile(fileName, dimMatrices+i,numMatrices+i);
		}
		
	}
}
