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
				Double randomValue = (randomGenerator.nextDouble() * randomGenerator.nextDouble())
						+ (randomGenerator.nextInt() * randomGenerator.nextInt());
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
		WriteMatricesFile.writeNewMatricesArguments("\n"+fileName+" "+numMatrices+" "+dimMatrices);
	}
		
	public static void main(String[] args) {
		if(args.length != 3){
			System.err.println("Argumento: \\nome_do_arquivo.txt \\dimensão_matriz \\numero_matrizes ");
			System.exit(1);
		}
		String fileName = args[0];
		int dimMatrices = Integer.parseInt(args[1]);
		int numMatrices = Integer.parseInt(args[2]);
		WriteMatricesFile(fileName, dimMatrices,numMatrices);
	}
}
