import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AdditionClient {

	public static void main(String[] args) {
		AdditionInterface hello;
		int result = 0;
		try {
			hello = (AdditionInterface) Naming.lookup("rmi://localhost/ADD");
			try {
				result = hello.add(9, 10);
			} catch (RemoteException e) {
				System.err.println("Falha na chamada de AdditionInterface.add - Erro de acesso remoto\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
			}
			System.out.println(result);
		} catch (MalformedURLException e) {
			System.err.println("Acesso ao AdditionServer falhou - Erro no url do servidor\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		} catch (RemoteException e) {
			System.err.println("Acesso ao AdditionServer falhou - Erro de acesso remoto\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		} catch (NotBoundException e) {
			System.err.println("Acesso ao AdditionServer falhou - Serviço inexistente\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		}
		
	}

}
