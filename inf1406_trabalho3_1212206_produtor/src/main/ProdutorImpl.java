package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import contracts.Configuracao;
import contracts.ConjuntoMatrizes;
import contracts.Matrix;
import contracts.Produtor;

public class ProdutorImpl implements Produtor {

	private File file;
	private volatile ArrayList<ConjuntoMatrizes> listaConjuntos;
	private volatile Iterator<ConjuntoMatrizes> it;
	private Object mutex = new Object();
	private volatile ConfiguracaoImpl config;
	private Runnable readerThread;
	
	public ProdutorImpl(String filenameMatrices) {
		this.config = new ConfiguracaoImpl();
		this.file = new File(filenameMatrices);
		this.listaConjuntos = new ArrayList<ConjuntoMatrizes>();	
		this.readerThread = getReaderThread();
	}

	public Configuracao exportConfig() throws RemoteException{
		return (Configuracao) UnicastRemoteObject.exportObject(config, 0);		
	}
	
	public void runReaderThread(){
		this.readerThread.run();
	}
	
	private Runnable getReaderThread(){
		Runnable readerThread = new Runnable() {
			private Scanner inConjunto;
			@Override
			public void run() {
				try {
					// Lemos do arquivo principal matrizes.txt
					@SuppressWarnings("resource")
					Scanner inLista = new Scanner(file);

					// Para cada conjunto de matrizes especificado
					while (inLista.hasNextLine()) {
						String line = inLista.nextLine();
						System.out.println("***Lendo "+line+"...");
						String[] argumentosConjunto = line.split(" ");
						// Recuperando os parametros de entrada
						inConjunto = new Scanner(new File(argumentosConjunto[0]));
						int size = Integer.parseInt(argumentosConjunto[1]);
						int quantMatrizes = Integer.parseInt(argumentosConjunto[2]);
						
						ConjuntoMatrizes conjunto = new ConjuntoMatrizesImpl();
						conjunto.setDimMatrices(size);
						while (inConjunto.hasNextLine()) {
							// Enquanto houver matrizes no arquivo
							for (int k = 0; k < quantMatrizes; k++) {
								Matrix matriz = new MatrixImpl();
								matriz.setDimension(size);
								for (int i = 0; i < size; i++) {
									StringTokenizer stringTokenizer = new StringTokenizer(this.inConjunto.nextLine(), " ");
									for (int j = 0; j < size; j++) {
										if (stringTokenizer.hasMoreTokens()) {
											Double value = Double.valueOf(stringTokenizer.nextToken());
											matriz.setValue(i, j, value);
										}
									}
								}
								conjunto.appendMatrix(matriz);
							}
						}
						listaConjuntos.add(conjunto);
						it = listaConjuntos.iterator();
						conjunto.print();
						Thread.sleep(config.getIntervalo());
					}
				} catch (FileNotFoundException e) {
					System.err.println("Erro ao ler arquivo de matrizes:\n" + e);
					System.exit(1);
				} catch (InterruptedException e) {
					System.err.println("Erro de interrupção do tempo de sleep:\n" + e);
				}
			}
		};
		return readerThread;
	}
	
	@Override
	public ConjuntoMatrizes obtemMatrizes() throws RemoteException {
		synchronized (mutex) {
			if (it.hasNext()) {
				return it.next();
			}
			return null;
		}
	}
}
