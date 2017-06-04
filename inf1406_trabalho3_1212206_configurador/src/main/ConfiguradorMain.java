package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import contracts.Configuracao;

public class ConfiguradorMain {
	
	private static void aplicaIntervalo(String host,int port, String webservice) throws RemoteException {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.println("Digite o intervalo do "+ webservice+": ");
		int intervalo = scan.nextInt();
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(host, port);
		} catch (RemoteException e) {
			throw new RemoteException("RMIRegistry de "+host+":"+port+" indisponivel", e);
		}
		Configuracao config = null;
		try {
			config = (Configuracao) registry.lookup(webservice);
		} catch (NotBoundException e) {
			throw new RemoteException(webservice+" não encontrado no RMIRegistry de "+host+":"+port, e);
		}
		try {
			config.aplicaIntervalo(intervalo);
		} catch (RemoteException e) {
			throw new RemoteException(webservice+" indisponivel no RMIRegistry de "+host+":"+port, e);
		}		
	}
	
	public static void main(String[] args) {

		Path policy = Paths.get(System.getProperty("user.dir"), "configurador.policy");
		Path codeBase = Paths.get(System.getProperty("java.class.path"));

		System.setProperty("java.security.policy", policy.toUri().toString());
		System.setProperty("java.rmi.server.codebase", codeBase.toUri().toString());
		System.setProperty("java.rmi.server.useCodebaseOnly", "false");

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
				
		if(args.length != 4){
			throw new IllegalArgumentException("A ordem dos argumentos requeridos são: \n"
					+ "\\Host_Produtor \\Port_Produtor "
					+ "\\Host_Consumidor \\Port_Consumidor ");
		}
		
		String hostProdutor = args[0];
		int portProdutor = Integer.parseInt(args[1]);
		String hostConsumidor = args[2];
		int portConsumidor = Integer.parseInt(args[3]);
		System.out.println(args[0]+" "+args[1]+" "+args[2]+" "+args[3]);
		
		String response = "";
		
		do{
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			System.out.println("Digite '1' para modificar o intervalo do Produtor\n"
					+ "Digite '2' para modificar o intervalo do Consumidor\n"
					+ "Digite '3' para terminar o Configurador");
			response = scan.nextLine();
			if(response.equals("1")){
				try {
					System.out.println("-- Aplicando intervalo do Produtor");
					aplicaIntervalo(hostProdutor, portProdutor,"ConfiguracaoProdutor");
				} catch (RemoteException e) {
					System.err.println(e);
				}				
			} else if(response.equals("2")){
				try {
					System.out.println("-- Aplicando intervalo do Produtor");
					aplicaIntervalo(hostConsumidor, portConsumidor,"ConfiguracaoConsumidor");
				} catch (RemoteException e) {
					System.err.println(e);
				}
			}
		}while(!response.equals("3"));
		if(response.equals("3")){
			System.out.println("Encerrando configurador...");
			System.exit(0);
		}		
	}
}
