package inf1406_trabalho2_1212206;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PUC-Rio INF1406 - Programação Distribuida e concorrente Exercício 2
 * 
 * @author Yang Ricardo Barcellos Miranda 
 *		   A classe MultiThreadSquareMatrixMultiplication oferece 2 soluções para
 *         multiplicação de matrizes:
 *         ->nonThreadMatrixMultiplication: que processa a multiplicação via 
 *         iterações, dentro do processo principal, aqui implementada para 
 *         simular o gasto de tempo pelas threads;
 *         ->ThreadMatrixMultiplication: que utiliza um pool de n threads fixas
 *         que executam a Callable ScalarProduct que é responsável por calcular
 *         uma celula de uma matriz produto entre outras duas matrizes. Para
 *         aumentar o tempo de trabalho, após o calculo da célula e antes do
 *         retorno da Callable, o método nonThreadScalarProduct é executado
 *         'virtualJobsLoops' vezes, a fim de aumentar a concorrencia entre os
 *         calculos; O retorno das Callables ocorrem em umm Future<Double> cujo
 *         valor, a cada iteração é armazenado numa estrutura auxiliar
 *         ArrayList<ArrayList<Future<Double>>>, que representa uma matriz,
 *         onde, ArrayList<ArrayList<Future<Double>>> é a matriz completa,
 *         ArrayList<Future<Double>> é uma linha da matriz, e cada
 *         Future<Double> de determinada linha é o elemento da i-ésima linha e
 *         j-ésima coluna, da matriz. A organização é semelhante a um objeto
 *         ArrayList<ArrayList<<Double>> Para cada par de matrizes, começando
 *         com matrices.get(0). a primeira matriz, e matrices.get(1), a segunda
 *         matriz é executado o conjunto de calculos pelas threads, ao fim de
 *         uma matriz parcial, essa matriz é armazenada em auxMatriz, e seu
 *         produto com matrices.get(m) é calculado e novamente armazenado em
 *         auxMatriz. => Callable<Double> worker = new ScalarProduct(i, j,
 *         auxMatrix, matrices.get(m)); A cada par de matrizes é executado:
 *         ArrayList<ArrayList<Future<Double>>> parcialFutureMatrix =
 *         createFutureParcialMatrix(); Para que a estrutura de valores ainda
 *         não calculados pela thread possa ser atualizada,resetando a indexação
 *         dos valores.adicionados. Ao adquirir todos os valores requisitados,
 *         os valores de parcialFutureMatrix são copiados para auxMatrix,
 *         auxMatrix.get(i).set(j, parcialFutureMatrix.get(i).get(j).get());
 *         cujos valores também foram resetados.
 * 
 *         Executors.newFixedThreadPool(numThreads); determina quantas threads
 *         executarão concorrentemente a Callable ScallarProduct e que serão
 *         reutilizadas para todos os calculos de céluas de cada par matrizes,
 *         caso não haja células o bastante, ou seja, o numero de threads seja
 *         superior ao numero de células, as threads que ainda não computaram
 *         entram em estado de espera, somente sendo chamadas quando chegar na
 *         sua vez da fila interna do pool de threads
 *         Executors.newFixedThreadPool(numThreads) Caso alguma thread falhe
 *         durante uma execução antes do shutdown, uma thread nova assume seu
 *         lugar para computar as tarefas restantes. As threads executam até uma
 *         chamada explicita pool.shutdown(); que desliga, conforme o término
 *         das tarefas de uma thread previamente executada, e impede a criação
 *         de novas threads. pool.awaitTermination(1, TimeUnit.HOURS); Bloqueia
 *         a criação de novas threads na threadpool após a solicitação de um
 *         shutdown, ou se um timeout ocorre, ou se uma thread é interrompida
 * 
 */
public class MultiThreadSquareMatrixMultiplication {
	// ARRAY LIST QUE ARMAZENA AS MATRIZES
	private static ArrayList<ArrayList<ArrayList<Double>>> matrices = new ArrayList<ArrayList<ArrayList<Double>>>();
	private static ArrayList<ArrayList<Double>> resultedMatrix;
	// Variaveis que controlam arquivos
	private static String pathFileMatrices = "matrizes.txt";
	private static String pathFileResult = "resultado.txt";
	// variaveis que controlam os aspectos principais do programa
	private static int matricesDimension = 20;
	private static int numThreads = 10;
	private static int numMatrices = 9;
	// busy job
	private static int virtualJobsLoops = matricesDimension;
	private static int[] countThreadsTasks;

	private static void readMatricesFile() {
		try {
			FileReader fileMatrices = new FileReader(pathFileMatrices);
			BufferedReader readFile = new BufferedReader(fileMatrices);

			// Array list que armazena as linhas de uma matriz
			ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
			String line;
			int auxColumn = 0;
			int auxLine = 0;
			int auxMatrix = 0;
			// enquanto houver linha para ser lida
			while ((line = readFile.readLine()) != null) {
				// System.out.println(line);
				ArrayList<Double> matrixLine = new ArrayList<Double>();
				// regex que pega os doubles de cada linha e adiciona ao
				// arraylist que representa a coluna da linha da matriz
				int contDblFound = 0;
				Matcher matcher = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?").matcher(line);// (-?(\\d)+(\\.)?(\\d)*)
																											// ((\\d)+(\\.(\\d)+)?)
																											// //[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?
				while (matcher.find()) {
					String dblStr = matcher.group();
					Double dbl = Double.valueOf(dblStr);
					contDblFound++;

					// se a quantidade de valor double encontrado na linha for
					// menor ou igual a dimensão da matriz quadrada
					// adiciona o valor a conluna da matriz
					if (contDblFound <= matricesDimension) {
						matrixLine.add(dbl);
						auxColumn++;
					}

					// System.out.println(dblStr);

					// se a coluna atingir a dimensão especificada para a matriz
					// quadrada, adiciona uma linha da matriz
					if (auxColumn == matricesDimension) {
						/*
						 * System.out.println(auxColumn +
						 * " colunas lidas da linha " + (auxLine + 1) +
						 * " da matriz " + (auxMatrix + 1));
						 */
						auxColumn = 0;
						matrix.add(matrixLine);
						auxLine++;
						// Se a linha foi lida apos o numero de matrizes
						// especificado for lido, o programa termina com erro
						if (auxMatrix == numMatrices && auxLine > 0) {
							System.err.println("Erro em relação ao numero de matrizes lidas!");
							System.err.println(
									auxMatrix + " matriz(es) foram lidas enquanto o especificado nos parametros foram "
											+ numMatrices);
							System.exit(1);
						}
					}

				} // while (matcher.find())

				// se a quantidade de valores double for diferente da dimensão
				// da matriz quadrada, aborta o programa
				if (contDblFound < matricesDimension || contDblFound > matricesDimension) {
					System.err.println("Formato do arquivo irregular! O numero de colunas da matriz " + (auxMatrix + 1)
							+ " está diferente do especificado nos parametros");
					System.err.println(contDblFound + " colunas lidas da matriz enquanto o especificado foram "
							+ matricesDimension);
					System.exit(1);
				}

				// se a quantidade de linhas para formar uma matriz quadrada for
				// atingida, adiciona a matriz ao arraylist de matrizes
				if (auxLine == matricesDimension) {
					// System.out.println(auxLine + " linhas lidas da matriz " +
					// (auxMatrix + 1));
					auxLine = 0;
					matrices.add(matrix);
					auxMatrix++;
					// instenciaa novo objeto de matriz para leitura
					matrix = new ArrayList<ArrayList<Double>>();
				}
			} // end while ((line = readFile.readLine()) != null)

			if (auxMatrix < numMatrices) {
				System.err.println("Erro em relação ao numero de matrizes lidas!");
				System.err.println(auxMatrix + " matriz(es) foram lidas enquanto o especificado nos parametros foram "
						+ numMatrices);
				System.exit(1);
			}

			fileMatrices.close();
		} catch (IOException e) {
			System.err.println("\"CaminhoArquivoMatrizes\"=não é um caminho válido");
			System.err.println("Mensagem de Erro:" + e.getMessage());
			System.exit(1);
		}
	}

	private static void writeFileProductMatrix() {

		try {
			FileWriter fw = new FileWriter(pathFileResult, false);
			BufferedWriter bw = new BufferedWriter(fw);
			String productMatrix = "";
			for (int i = 0; i < matricesDimension; i++) {
				ArrayList<Double> matrixLine = resultedMatrix.get(i);
				for (int j = 0; j < matricesDimension; j++) {
					productMatrix = productMatrix + matrixLine.get(j).toString() + " ";
					if (j == (matricesDimension - 1))
						productMatrix = productMatrix + "\n";
				}
			}
			bw.write(productMatrix);
			bw.close();
			fw.close();
			System.out.println("Arquivo resultado.txt foi criado com sucesso");
		} catch (IOException e) {
			System.err.println("Erro ao escrever arquivo resultado.txt: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

	}

	private static void printMatrix(ArrayList<ArrayList<Double>> matrix, String idMatrix) {
		String sMatrix = "Matriz " + idMatrix + "\n";
		for (int i = 0; i < matricesDimension; i++) {
			ArrayList<Double> matrixLine = matrix.get(i);
			for (int j = 0; j < matricesDimension; j++) {
				sMatrix = sMatrix + " | " + matrixLine.get(j).toString();
				if (j == (matricesDimension - 1))
					sMatrix = sMatrix + " | \n";
			}
		}
		System.out.println(sMatrix);
	}

	private static ArrayList<ArrayList<Double>> generateRandomSquareMatrix(int matrixDim) {
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
		return matrix;
	}

	@SuppressWarnings("unused")
	private static void printNRandomMatrices(int numOfMatrices, int matrixDim) {
		String sMatrix = "";
		for (int m = 0; m < numOfMatrices; m++) {
			ArrayList<ArrayList<Double>> matrix = generateRandomSquareMatrix(matrixDim);
			for (int i = 0; i < matrixDim; i++) {
				ArrayList<Double> matrixLine = matrix.get(i);
				for (int j = 0; j < matrixDim; j++) {
					sMatrix = sMatrix + matrixLine.get(j).toString() + " ";
					if (j == (matrixDim - 1))
						sMatrix = sMatrix + "\n";
				}
			}
		}
		System.out.println(sMatrix);
	}

	@SuppressWarnings("unused")
	private static void printResultCell(String idMatrix, int line, int column, Double resultCell) {
		System.out.println("[Matriz " + idMatrix + " Linha: " + line + " Coluna: " + column + "] = " + resultCell);
	}

	private static ArrayList<ArrayList<Double>> createResultMatrix() {
		ArrayList<ArrayList<Double>> newMatrix = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < matricesDimension; i++) {
			ArrayList<Double> matrixLine = new ArrayList<Double>();
			for (int j = 0; j < matricesDimension; j++) {
				matrixLine.add(0.0);
			}
			newMatrix.add(matrixLine);
		}
		return newMatrix;
	}

	private static ArrayList<ArrayList<Future<Double>>> createFutureParcialMatrix() {
		ArrayList<ArrayList<Future<Double>>> newMatrix = new ArrayList<ArrayList<Future<Double>>>();
		for (int i = 0; i < matricesDimension; i++) {
			ArrayList<Future<Double>> matrixLine = new ArrayList<Future<Double>>();
			newMatrix.add(matrixLine);
		}
		return newMatrix;
	}

	private static void nonThreadScalarProduct(ArrayList<ArrayList<Double>> matrix1,
			ArrayList<ArrayList<Double>> matrix2, ArrayList<ArrayList<Double>> matrixF, int line, int column) {
		Double resultCell = 0.0;
		for (int i = 0; i < matricesDimension; i++) {
			Double scalarStep = matrix1.get(line).get(i) * matrix2.get(i).get(column);
			resultCell += scalarStep;
		}
		matrixF.get(line).set(column, resultCell);
	}

	private static ArrayList<ArrayList<Double>> nonThreadMatrixMultiplication() {
		/*
		 * oldStepMatrix é iniciado como a primeira matriz do arquivo e é
		 * atualizado conforme a multiplicação total entre as matrizes
		 */
		ArrayList<ArrayList<Double>> oldStepMatrix = matrices.get(0);
		ArrayList<ArrayList<Double>> newStepMatrix = createResultMatrix();
		/*
		 * cada oldStepMatrix é uma matriz resulado parcial da multiplicação
		 * entre 2 matrizes
		 */
		for (int m = 1; m < numMatrices; m++) {
			// System.out.println((m < 2 ? "M" + (m - 1) + "* M" + m : "Mstp" +
			// (m - 1) + "* M" + m));
			for (int i = 0; i < matricesDimension; i++) {
				for (int j = 0; j < matricesDimension; j++) {
					nonThreadScalarProduct(oldStepMatrix, matrices.get(m), newStepMatrix, i, j);
				}
			}
			oldStepMatrix = newStepMatrix;
			newStepMatrix = createResultMatrix();
		}
		return oldStepMatrix;
	}

	private static ArrayList<ArrayList<Double>> ThreadMatrixMultiplication() {
		// Cria ThreadPool com 'numThreads' fixas
		ExecutorService pool = Executors.newFixedThreadPool(numThreads);

		/* Inicio da computação das threads */

		// auxMatrix é um objeto auxiliador que começa com a primeira matriz do
		// arquivo e vai sendo atualizada conforme as multiplicações
		ArrayList<ArrayList<Double>> auxMatrix = matrices.get(0);
		for (int m = 1; m < numMatrices; m++) {// para cada matriz listada
			// Gera e atualiza a cada iteração a estrutura para organizar os
			// retornos futuros das threads
			ArrayList<ArrayList<Future<Double>>> parcialFutureMatrix = createFutureParcialMatrix();
			for (int i = 0; i < matricesDimension; i++) {// para cada linha de
															// uma matriz
				for (int j = 0; j < matricesDimension; j++) {// para cada coluna
																// de uma matriz
					Callable<Double> worker = new ScalarProduct((m + 1), i, j, auxMatrix, matrices.get(m));
					Future<Double> dblValue = pool.submit(worker);
					parcialFutureMatrix.get(i).add(dblValue);
				}
			}

			// prepara auxMatrix para receber os valores calculados pelas
			// threads e armazenados em parcialFutureMatrix
			auxMatrix = createResultMatrix();
			for (int i = 0; i < matricesDimension; i++) {
				for (int j = 0; j < matricesDimension; j++) {
					try {
						auxMatrix.get(i).set(j, parcialFutureMatrix.get(i).get(j).get());
					} catch (InterruptedException e) {
						System.err.println("Erro de interrupção: " + e.getMessage());
						e.printStackTrace();
						System.exit(1);
					} catch (ExecutionException e) {
						System.err.println("Erro de execução: " + e.getMessage());
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
			// printMatrix(auxMatrix, "Multiplicação Parcial: " + m);
		}

		pool.shutdown();
		try {
			pool.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			System.err.println("Erro de interrupção: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		return auxMatrix;
	}

	private static class ScalarProduct implements Callable<Double> {

		private int line;
		private int column;
		private int step;
		private ArrayList<ArrayList<Double>> matrix1, matrix2;

		public ScalarProduct(int step, int line, int column, ArrayList<ArrayList<Double>> matrix1,
				ArrayList<ArrayList<Double>> matrix2) {
			this.column = column;
			this.line = line;
			this.matrix1 = matrix1;
			this.matrix2 = matrix2;
			this.step = step;
		}

		@Override
		public Double call() throws Exception {
			Double resultCell = 0.0;

			for (int i = 0; i < matricesDimension; i++) {
				resultCell += matrix1.get(line).get(i) * matrix2.get(i).get(column);
			}

			// gastar tempo virtualmente
			for (int virtualJob = 0; virtualJob < virtualJobsLoops; virtualJob++)
				nonThreadMatrixMultiplication();

			System.out.println(
					Thread.currentThread().getName() + "  M" + step + "[" + line + "," + column + "]: " + resultCell);
			int idThread = Integer.parseInt(Thread.currentThread().getName().split("-thread-")[1]) - 1;
			countThreadsTasks[idThread]++;
			return resultCell;
		}

	}

	public static void main(String[] args) {

//		if (args.length != 4) {
//			System.err.println(
//					"Modo de uso: java MultiThreadMatrixMultiplication \"CaminhoArquivoMatrizes\" \"DimensãoMatrizesNxN\" \"NumThreads\" \"NumMatrizes\"");
//			System.exit(1);
//		}
//
//		List<String> argsParam = Arrays.asList(args);
//		pathFileMatrices = argsParam.get(0);
//		matricesDimension = Integer.parseInt(argsParam.get(1));
//		numThreads = Integer.parseInt(argsParam.get(2));
//		numMatrices = Integer.parseInt(argsParam.get(3));

		// ler arquivo de matrizes
		readMatricesFile();
		int m = 0;
		for (ArrayList<ArrayList<Double>> matrix : matrices) {
			printMatrix(matrix, String.valueOf(m));
			m++;
		}
		countThreadsTasks = new int[numThreads];

		long startTimeThread = System.currentTimeMillis();
		// computa multiplicação de matrizes
		resultedMatrix = ThreadMatrixMultiplication();
		long endTimeThread = System.currentTimeMillis();
		System.out.println("\nO tempo para a multiplicação das " + numMatrices + " matrizes quadradas de dimensão "
				+ matricesDimension + " pelas " + numThreads + " Threads foi de: " + (endTimeThread - startTimeThread)
				+ " millisegundos");
		// escreve arquivo
		writeFileProductMatrix();
		printMatrix(resultedMatrix, "Produto Final");
		for (int i = 0; i < numThreads; i++)
			System.out.println("Thread " + i + " computou " + countThreadsTasks[i] + " tarefas");
		// printNRandomMatrices(9, 20);
	}

}
