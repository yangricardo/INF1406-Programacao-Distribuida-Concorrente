package produtor;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import contracts.ConjuntoMatrizes;
import contracts.Matrix;
import contracts.ProdutorInterface;

public class Produtor implements ProdutorInterface {
	
	private Scanner in;
	private File file;
	private ArrayList<ConjuntoMatrizes> listaConjuntos;
	private Iterator<ConjuntoMatrizes> it;
	private Object mutex = new Object();
	
	public Produtor(String[] args) {
		this.file = new File(args[0]);
		this.listaConjuntos = new ArrayList<ConjuntoMatrizes>();
		
        try {
        	//Lemos do arquivo principal matrizes.txt
            Scanner inLista = new Scanner(file);
			
            //Para cada conjunto de matrizes especificado
			while(inLista.hasNextLine()) {
				String line = inLista.nextLine();
				ConjuntoMatrizes conjunto = new ConjuntoMatrizesImpl();
	            String[] argumentosConjunto = line.split(" ");
				//Recuperando os parametros de entrada
				File file = new File(argumentosConjunto[0]);
				int size = Integer.parseInt(argumentosConjunto[1]);
				int quantMatrizes = Integer.parseInt(argumentosConjunto[2]);
				Scanner inConjunto = new Scanner(file);
				
				while(inConjunto.hasNextLine()) {
		            //Enquanto houver matrizes no arquivo
		            for(int i = 0; i < quantMatrizes; i++) {
		            	conjunto.appendMatrix(construirMatriz(inConjunto, size));
		            }
				}
				listaConjuntos.add(conjunto);
	            
	            Thread.sleep(1);
			}
			
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

    private static Matrix construirMatriz(Scanner in, int size) throws FileNotFoundException {

        Matrix  matriz = new MatrixImpl();
        
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
            	if(in.hasNextDouble()) {
            		matriz.addToInnerArray(i, j, in.nextDouble());
            	}
            }
        }

        return matriz;
    }
}
