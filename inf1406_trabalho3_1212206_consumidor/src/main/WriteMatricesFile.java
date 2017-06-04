package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import contracts.ConjuntoMatrizes;
import contracts.Matrix;

public class WriteMatricesFile {

	private static FileWriter fw;
	private static BufferedWriter bw;
	private static String nameFile;
	
	public static void createFile(String nameFile){
		try{
			WriteMatricesFile.nameFile = nameFile;
			fw = new FileWriter(nameFile,false);
			bw = new BufferedWriter(fw);			
		} catch (IOException e) {
			System.err.println("Erro ao criar arquivo "+ nameFile+"\n" + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}		
	}
	

	public static void openFile(String nameFile,boolean append){
		try{
			WriteMatricesFile.nameFile = nameFile;
			fw = new FileWriter(nameFile,append);
			bw = new BufferedWriter(fw);			
		} catch (IOException e) {
			System.err.println("Erro ao abrir arquivo "+ nameFile+"\n" + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}		
	}
	
	public static void writeNewMatricesArguments(String arguments){
		try {
			bw.write(arguments);
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.err.println("Erro ao escrever arquivo"+ nameFile +"\n"+ e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public static void writeMatrices(ConjuntoMatrizes matrices){
		try{
			ArrayList<Matrix> matrixList = matrices.getMatrices();
			for(Matrix matrix : matrixList){
				bw.write(matrix.toString());
			}
			bw.close();
			fw.close();			
		} catch (IOException e) {
			System.err.println("Erro ao escrever arquivo"+ nameFile +"\n"+ e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}				
	}
	
	public static void writeMatrix(Matrix matrix){
		try{
			bw.write(matrix.toString());
			bw.close();
			fw.close();			
		} catch (IOException e) {
			System.err.println("Erro ao escrever arquivo"+ nameFile +"\n"+ e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}				
	}
}
