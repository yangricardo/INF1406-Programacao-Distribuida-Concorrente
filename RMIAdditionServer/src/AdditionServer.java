import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class AdditionServer {

	public static void main(String[] args) {
 
		try {
			Addition hello = new Addition();
			Naming.rebind("rmi://localhost/ADD", hello);
			System.out.println("AdditionServer est� pronto!");
		} catch (RemoteException e) {
			System.err.println("Inicializa��o do AdditionServer falhou - Erro de acesso remoto\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		} catch (MalformedURLException e) {
			System.err.println("Inicializa��o do Falha no servidor AdditionServer- Erro na url do rmi\n"+e.getMessage()+"\n"+e.getCause()+"\n"+e.getLocalizedMessage());
		}
		
	}
}
