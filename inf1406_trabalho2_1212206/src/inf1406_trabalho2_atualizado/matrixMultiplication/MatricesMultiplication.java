package inf1406_trabalho2_atualizado.matrixMultiplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import inf1406_trabalho2_atualizado.WriteMatricesFile.WriteMatricesFile;
import inf1406_trabalho2_atualizado.model.Matrices;
import inf1406_trabalho2_atualizado.model.Matrix;

public class MatricesMultiplication {

	private static Matrices matrices;

	// variaveis que controlam os aspectos principais do programa
	private static int matricesDimension = 3;	
	private static ExecutorService poolMultiplier;

	private static void processMatricesFile(String pathFileMatrices,int matricesDimension, int numThreads, int numMatrices) {
		MatricesMultiplication.matricesDimension = matricesDimension;
		//inicia pool de threads
		poolMultiplier = Executors.newFixedThreadPool(numThreads);
		
		try {
			FileReader fileMatrices = new FileReader(pathFileMatrices);
			BufferedReader readFile = new BufferedReader(fileMatrices);
			String line;
			int auxColumn = 0;
			int auxLine = 0;
			int contMatrices = 0;
			//objeto que armazena lista de matrizes
			matrices = new Matrices();
			try {
				Matrix matrix = new Matrix();
				while((line = readFile.readLine()) != null){
					StringTokenizer stringTokenizer = new StringTokenizer(line," ");
					if(stringTokenizer.countTokens() != matricesDimension){
						//Se algum numero de elementos por linha for diferente, aborta
						System.err.println("Formato do arquivo irregular! O numero de colunas da matriz " + (stringTokenizer.countTokens())
								+ " está diferente do especificado nos parametros");
						System.exit(1);	
					}
					while(stringTokenizer.hasMoreTokens()){
						//processa doubles contidos nas linhas separados por espaço
						Double dbl = Double.valueOf(stringTokenizer.nextToken());
						if(auxColumn == 0)
							//se o numero de elementos processado para a linha for 0 adiciona elemento em nova linha
							matrix.addNewLine(dbl);
						else
							//se o numero de elementos processados não for 0, adiciona a linha atual da matriz
							matrix.addValue(dbl);
						auxColumn++;
						if(auxColumn == matricesDimension){
							//se auxColumn atingiu matrices dimension, contabiliza mais uma linha lida para a matriz
							auxColumn = 0;
							auxLine++;
						}
					}	
					
					if(auxLine == matricesDimension){
						//se o numero de linhas lidas for o mesmo que a dimensão da matriz, adiciona matriz lida ao conjunto matrices
						auxLine = 0;
						matrices.appendMatrix(matrix);
						contMatrices++;
						System.out.println(contMatrices+" lidas!");
						matrices.print();
						if(matrices.getMatrices().size() > 1){
							//se houverem matrizes para serem calculadas
							System.out.println("************Multiplication "+(contMatrices-1));
							ThreadMatrixMultiplication();
							matrices.getMatrix(0).print();
							//ao ler todas as matrizes sai do processo de leitura, evita nullpoint
							if(contMatrices == numMatrices)
								continue;
						}
						//instancia outra matriz
						matrix = new Matrix();
					}					
				}
				if(contMatrices != numMatrices){
					System.err.println("Quantidade de matrizes lidas diferente do especificado");
					System.exit(1);
				}
				poolMultiplier.shutdown();
				try {
					poolMultiplier.awaitTermination(5, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					System.err.println("Erro de interrupção: " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
				}
				matrices.print();
			} catch (IOException e) {
				System.err.println("\"CaminhoArquivoMatrizes\"=não é um caminho válido");
				System.err.println("Mensagem de Erro:" + e.getMessage());
				System.exit(1);
			}
			WriteMatricesFile.createFile("resultado.txt");
			WriteMatricesFile.writeMatrices(matrices);
		} catch (FileNotFoundException e) {
			System.err.println("\"CaminhoArquivoMatrizes\"=não é um caminho válido");
			System.err.println("Mensagem de Erro:" + e.getMessage());
			System.exit(1);
		}
	}

	private static ArrayList<ArrayList<Future<Double>>> createFutureParcialMatrix() {
		ArrayList<ArrayList<Future<Double>>> newMatrix = new ArrayList<ArrayList<Future<Double>>>();
		for (int i = 0; i < matricesDimension; i++) {
			ArrayList<Future<Double>> matrixLine = new ArrayList<Future<Double>>();
			newMatrix.add(matrixLine);
		}
		return newMatrix;
	}
		
	private static void ThreadMatrixMultiplication(){
		ArrayList<ArrayList<Future<Double>>> parcialFutureMatrix = createFutureParcialMatrix();
		matrices.getMatrix(0);
		matrices.getMatrix(1);
		for(int line = 0; line < matricesDimension; line++){
			for(int column = 0; column < matricesDimension; column++){
				Callable<Double> worker = new ScalarProduct(line, column, matricesDimension,matrices.getMatrix(0),matrices.getMatrix(1));
				Future<Double> dblValue = poolMultiplier.submit(worker);
				parcialFutureMatrix.get(line).add(dblValue);
			}			
		}
		
		Matrix auxMatrix = new Matrix();
		
		for(int line = 0; line < matricesDimension; line++){
			for(int column = 0; column < matricesDimension; column++){
				try {
					if(column == 0 || column == matricesDimension)
						auxMatrix.addNewLine(parcialFutureMatrix.get(line).get(column).get());
					else
						auxMatrix.addValue(parcialFutureMatrix.get(line).get(column).get());
				} catch (InterruptedException e) {
					//lançada ao ocorrer uma thread em estado de waiting/sleeping que é interrompida
					System.err.println("Erro de interrupção: " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
				} catch (ExecutionException e) {
					//sai por bugs de execução
					System.err.println("Erro de execução: " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		//substitui a matriz na posicao 0 pela matriz resultado e remove a matriz na posicao 1 de matrices
		//ao final, o conjunto matrices so deve possuir uma unica matriz
		matrices.putResultedMatrix(0, 1, auxMatrix);
	}
	
	public static void main(String[] args) {
		processMatricesFile("matrices.txt", 3, 6, 6);
	}
}