package compute;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import computeContracts.Compute;
import java.util.Properties;

public class ComputePi {


	public static void main(String[] args) {
		Properties properties = System.getProperties();
		
		String host = "localhost";
		int port = 1099;
		String webService = "Compute";
		
		Path policy = Paths.get(System.getProperty("user.dir"),"client.policy");
		Path codeBase = Paths.get(System.getProperty("java.class.path"));
		System.setProperty("java.security.policy",policy.toUri().toString());
		System.setProperty("java.rmi.server.codebase",codeBase.toUri().toString());		
		//System.setProperty("java.rmi.server.useCodebaseOnly", "false");
		
		properties.forEach((k,v)->System.out.println(k+" '"+v+"'"));
		if(System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}
				
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(host,port);
		} catch (RemoteException e) {
			System.err.println("Erro ao acessar RMIRegistry");
			e.printStackTrace();
		}
		Compute compute = null;
		try {
			compute = (Compute) registry.lookup(webService);
		} catch (AccessException e) {
			System.err.println("Acesso negado ao client");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.err.println("Erro ao acessar objeto remoto");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("Serviço inexistente no RMIRegistry do endereço IP");
			e.printStackTrace();
		}
		
		Pi task = new Pi(5);
		BigDecimal piComputed = null; 
		try {
			piComputed = compute.executeTask(task);
		} catch (RemoteException e) {
			System.out.println("Serviço Compute indiponível em "+host+":"+port);
			e.printStackTrace();
		}
		System.out.println(piComputed);
	}

}
