package main;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import contracts.Callback;
import contracts.Configuracao;
import contracts.ConjuntoMatrizes;
import contracts.Execucao;
import contracts.Matrix;
import contracts.Produtor;
import contracts.Resultado;

public class ConsumidorImpl {

	private volatile ConfiguracaoImpl config;
	private Produtor produtorStub;
	private Execucao execucaoStub;
	private static String tableFormat = "%30s%30s%30f\n";
	private ConjuntoMatrizes conjunto;
	private ArrayList<ConjuntoMatrizes> conjuntos;
	private Runnable requestConjunto;
	
	public ConsumidorImpl(Produtor produtorStub,Execucao execucaoStub) {
		this.config = new ConfiguracaoImpl();
		this.requestConjunto = getConjuntoMatrizes();
		this.produtorStub = produtorStub;
		this.execucaoStub = execucaoStub;
		this.conjuntos = new ArrayList<ConjuntoMatrizes>();
	}

	public Configuracao exportConfig() throws RemoteException{
		return (Configuracao) UnicastRemoteObject.exportObject(config, 0);		
	}
	
	public void runConsumidor(){
		requestConjunto.run();
	}
	
	private Runnable getConjuntoMatrizes(){
		Runnable requestMatrices = new Runnable() {	
		
			@Override
			public void run() {
				while(true){
					try {
						//tenta obter um conjunto
						ConjuntoMatrizes temp = produtorStub.obtemMatrizes();
						if(conjunto != null){
							conjuntos.add(temp);
						} else
							System.err.println("Servidor Produtor sem Conjuntos de Matrizes Disponivel até o momento");
						
						//se há conjunto para o consumidor processar, executa a multiplicaão de matrizes
						if(conjuntos.iterator().hasNext()){
							conjunto = conjuntos.remove(0);
							matricesMultiplication().run();
						}
					} catch (RemoteException e) {
						System.err.print("Objeto Remoto do Servidor Produtor indisponivel:\n"+e);					
					} finally{
						//dorme por um intervalo caso nao consiga um conjunto ou apos multiplicar um conjunto		
						try {
							Thread.sleep(config.getIntervalo());
						} catch (InterruptedException e) {
							System.err.println("Erro de interrupção:\n"+e);
						}
					}
				}
			}
		};		
		return requestMatrices;
	}
	
	private Runnable matricesMultiplication(){
		Runnable multiplierThread = new Runnable() {
			Object MUTEX = new Object();
			@Override
			public void run() {
				try {
					int num = conjunto.getNumMatrices();
					int dim = conjunto.getDimMatrices();
					Matrix resultadoFinal = new MatrixImpl();
					Matrix result = null;
					for(int matrixIndex = 1; matrixIndex < num; matrixIndex++){
						synchronized (MUTEX) {
							if(matrixIndex == 1)
								//fixa a matriz na primeira posicao do conjunto
								result = conjunto.getMatrices().get(0);
							else
								//ou a calculada nos resultados anteriores
								result = (MatrixImpl)result;
							//Obtem matriz a ser multiplicada
							Matrix multiplied = conjunto.getMatrices().get(matrixIndex);
							HashMap<Callback, Semaphore> tasks = new HashMap<Callback, Semaphore>();
							for(int i = 0; i < dim; i++) {
								for(int j = 0; j < dim; j++) {
									Resultado resultado = new ResultadoImpl(i, j, 0.0);
									Semaphore sema = new Semaphore(0);
									Callback callbackTask = new CallbackImpl(resultado, sema);
									Callback callbackStub = (Callback) UnicastRemoteObject.exportObject(callbackTask, 0);
									ScalarProduct task = new ScalarProduct(i, j, dim, result, multiplied,callbackStub);
									try {
										execucaoStub.execute((Runnable)task);
										tasks.put(callbackTask, sema);
									} catch (RemoteException e){
										System.out.println("Servidor de Execução indiponível");
										e.printStackTrace();
									}
								}
							}
							Iterator<Callback> callbacks = tasks.keySet().iterator();
							System.out.println("\n-----------------------------");
							System.out.format("Execução das tarefas - Matrizes %d & %d\n", matrixIndex, matrixIndex + 1);
							System.out.println("-----------------------------");
							System.out.format("%30s%30s%30s\n", "Timestamp", "Célula", "Resultado");
							
							while(callbacks.hasNext()) {
								Callback c = callbacks.next();
								tasks.get(c).acquire();
								
								Resultado resultado = c.getResultado();
								String celula = "["+resultado.getLine()+","+resultado.getColumn()+"]";
								System.out.format(tableFormat, new Timestamp(System.currentTimeMillis()), celula, resultado.getResultado());
								result.setValue(resultado.getLine(), resultado.getColumn(), resultado.getResultado());
								callbacks.remove();
							}
							resultadoFinal = (MatrixImpl)result;
						}//END synchornized	
					}//END for
					System.out.println("\n-----------------------------");
					System.out.println("Resultado Final");
					System.out.println("-----------------------------");
					resultadoFinal.print();
					String fileName = "resultado_".concat(new Date().toString().replaceAll(" ",	"_").replaceAll(":", "-").concat(".txt"));
					WriteMatricesFile.createFile(fileName);
					WriteMatricesFile.writeMatrix(resultadoFinal);
					System.out.println("Arquivo com matriz resultado '"+fileName+"' criado com sucesso!");
				} catch (RemoteException e) {
					System.err.print("Erro ao obter matrizes do produtor:\n"+e);
					System.exit(1);
				} catch (InterruptedException e) {
					System.err.print("Erro ao calcular matrizes do produtor:\n"+e);
					System.exit(1);
				}
				
			}
		};		
		return multiplierThread;		
	}	
}
