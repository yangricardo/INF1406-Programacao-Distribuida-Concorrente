import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AdditionClient {

	public static void main(String[] args) {
			
		int result = 0;
		try {
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1100);
			AdditionInterface stub = (AdditionInterface) registry.lookup("Add");
			
			try {
				result = stub.add(9, 10);
			} catch (RemoteException e) {
				System.err.println("Falha na chamada de AdditionInterface.add - Erro de acesso remoto\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
			}
			System.out.println(result);
			
		} catch (RemoteException e) {
			System.err.println("Acesso ao AdditionServer falhou - Erro de acesso remoto\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		} catch (NotBoundException e) {
			System.err.println("Acesso ao AdditionServer falhou - Serviço inexistente\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		}
		
	}

}
