package inf1406_trabalho3_1212206_produtor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProdutorServer {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 1099;
		String webService = "Produtor";
		Path policy = Paths.get(System.getProperty("user.dir"), "produtor.policy");
		Path codeBase = Paths.get(System.getProperty("java.class.path"));

		System.setProperty("java.security.policy", policy.toUri().toString());
		System.setProperty("java.rmi.server.codebase", codeBase.toUri().toString());
		//System.setProperty("java.rmi.server.useCodebaseOnly", "false");

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		
	}

}
