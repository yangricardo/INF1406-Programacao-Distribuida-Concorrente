package inf1406_trabalho2_atualizado.WriteMatricesFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import inf1406_trabalho2_atualizado.model.Matrices;
import inf1406_trabalho2_atualizado.model.Matrix;

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
	
	public static void writeMatrices(Matrices matrices){
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
