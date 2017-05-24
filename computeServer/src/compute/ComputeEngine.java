package compute;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import computeContracts.Compute;
import computeContracts.Task;

public class ComputeEngine implements Compute {

	public ComputeEngine() {
		super();
	}

	@Override
	public <T> T executeTask(Task<T> t) throws RemoteException {
		if (t == null) {
			throw new IllegalArgumentException("task is null");
		}
		return t.execute();
	}

	public static void main(String[] args) throws RemoteException, AccessException {

		String host = "localhost";
		int port = 1099;
		String webService = "Compute";
		Path policy = Paths.get(System.getProperty("user.dir"), "server.policy");
		Path codeBase = Paths.get(System.getProperty("java.class.path"));

		System.setProperty("java.security.policy", policy.toUri().toString());
		System.setProperty("java.rmi.server.codebase", codeBase.toUri().toString());
		System.setProperty("java.rmi.server.useCodebaseOnly", "false");
		//System.setProperty("java.rmi.server.hostname", host);

		/**
		 * If an RMI program does not install a security manager, RMI will not
		 * download classes (other than from the local class path) for objects
		 * received as arguments or return values of remote method invocations.
		 * This restriction ensures that the operations performed by downloaded
		 * code are subject to a security policy.
		 */
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		Compute engine = new ComputeEngine();
		Compute engineStub = null;
		try {
			engineStub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
		} catch (RemoteException e) {
			System.err.println("Erro ao gerar objeto remoto");
			e.printStackTrace();
		}
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			try {
				registry = LocateRegistry.getRegistry(port);
			} catch (RemoteException e1) {
				System.err.println("Erro ao receber referencia para o RMIRegistry na porta " + port);
			}
		}
		try {
			registry.rebind(webService, engineStub);
			System.out.println(webService + " está ativado na porta " + port + "!");
		} catch (AccessException e) {
			System.err.println("Erro de permissão para executar ação de rebind para " + webService
					+ " no RMIRegistry em " + port + ":" + host);
			e.printStackTrace();
		} catch (RemoteException e) {
			System.err.println("Erro ao exportar stub de " + webService + " no RMIRegistry em " + port + ":" + host);
			e.printStackTrace();
		}

		class Hello implements Task<String>, Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String name;

			public Hello() {
				super();
				this.name = " World!";
			}

			public Hello(String name) {
				super();
				this.name = " " + name + "!";
			}

			@Override
			public String execute() {
				return "Hello" + name;
			}

		}

		Registry registry2 = LocateRegistry.getRegistry(host, port);
		try {
			Compute helloStub = (Compute) registry2.lookup(webService);
			Hello hello = new Hello();
			String msg = helloStub.executeTask(hello);
			System.out.println(msg);
			hello = new Hello("Yang");
			msg = helloStub.executeTask(hello);
			System.out.println(msg);

		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
