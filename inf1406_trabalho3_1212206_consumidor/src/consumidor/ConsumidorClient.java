package consumidor;

import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import contracts.ProdutorInterface;
import model.ConjuntoMatrizes;

public class ConsumidorClient {

	public static void main(String[] args) {
			
		ConjuntoMatrizes conjunto = new ConjuntoMatrizes();
		try {
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1101);
			ProdutorInterface stub = (ProdutorInterface) registry.lookup("Produtor");
			
			try {
				conjunto = stub.obtemMatrizes();
				if(conjunto != null) {
					conjunto.print();					
				}
				
			} catch (RemoteException e) {
				System.err.println("Falha na chamada de ProdutorInterface.obtemMatrizes - Erro de acesso remoto\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (RemoteException e) {
			System.err.println("Acesso ao ProdutorServer falhou - Erro de acesso remoto\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		} catch (NotBoundException e) {
			System.err.println("Acesso ao ProdutorServer falhou - Serviço inexistente\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		}
		
	}
}
