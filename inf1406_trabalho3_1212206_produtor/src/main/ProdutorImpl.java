package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import contracts.ConjuntoMatrizes;
import contracts.Matrix;
import contracts.Produtor;

public class ProdutorImpl implements Produtor {
	
	private Scanner inConjunto;
	private File file;
	private ArrayList<ConjuntoMatrizes> listaConjuntos;
	private Iterator<ConjuntoMatrizes> it;
	private Object mutex = new Object();
	
	public ProdutorImpl(String[] args) {
		this.file = new File(args[0]);
		this.listaConjuntos = new ArrayList<ConjuntoMatrizes>();
		System.out.println(this.file.getName());
        try {
        	//Lemos do arquivo principal matrizes.txt
            @SuppressWarnings("resource")
			Scanner inLista = new Scanner(file);
			
            //Para cada conjunto de matrizes especificado
            while(inLista.hasNextLine()){
				String line = inLista.nextLine();
				String[] argumentosConjunto = line.split(" ");
				//Recuperando os parametros de entrada
				File file = new File(argumentosConjunto[0]);
				int size = Integer.parseInt(argumentosConjunto[1]);
				int quantMatrizes = Integer.parseInt(argumentosConjunto[2]);
				inConjunto = new Scanner(file);
				
				inConjunto = new Scanner(new File(argumentosConjunto[0]));
				ConjuntoMatrizes conjunto = new ConjuntoMatrizesImpl();
				conjunto.setDimMatrices(size);				
				while(inConjunto.hasNextLine()){
		            //Enquanto houver matrizes no arquivo
		            for(int k = 0; k < quantMatrizes; k++) {
		            	 Matrix  matriz = new MatrixImpl();
		                 matriz.setDimension(size);
		                 for(int i = 0; i < size; i++) {
		                     for(int j = 0; j < size; j++) {
		                     	if(this.inConjunto.hasNextDouble()) {
		                     		matriz.setValue(i, j, this.inConjunto.nextDouble());
		                     	}
		                     }
		                 }
		            	conjunto.appendMatrix(matriz);  	
		            }
				}
				listaConjuntos.add(conjunto);	            
	            Thread.sleep(1);
			}			
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao ler arquivo de matrizes:\n"+e);
            System.exit(1);
        } catch (InterruptedException e) {
			System.err.println("Erro de interrupção do tempo de sleep:\n"+e);
		}

		this.it = this.listaConjuntos.iterator();
		for(ConjuntoMatrizes c : listaConjuntos) {
			c.print();
		}
	}

	@Override
	public ConjuntoMatrizes obtemMatrizes() throws RemoteException {
		synchronized(mutex) {			
			if(it.hasNext()) {
				return it.next();			
			}
			return null;
		}
	}
}
