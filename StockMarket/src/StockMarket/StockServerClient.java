package StockMarket;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.omg.CORBA.ORB;


public class StockServerClient {

	public static void main(String[] args) {
		
		Properties orbProps = new Properties();
		
		orbProps.setProperty("org.omg.CORBA.ORBClass","org.jacorb.orb.ORB");
		orbProps.setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
		ORB orb = ORB.init(args, orbProps);
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
			String ior = reader.readLine();
			
			org.omg.CORBA.Object obj = orb.string_to_object(ior);
			
			
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo inexistente:\n"+e);
		} catch (IOException e) {
			System.err.println("Erro ao ler arquivo:\n"+e);
		}
		
	}

}
