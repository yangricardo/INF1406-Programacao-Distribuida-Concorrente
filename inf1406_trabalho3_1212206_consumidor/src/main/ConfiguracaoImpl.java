package main;

import java.rmi.RemoteException;

import contracts.Configuracao;

public class ConfiguracaoImpl implements Configuracao{

	private volatile int intervalo;
	
	public ConfiguracaoImpl() {
		super();
		this.intervalo = 500;
	}

	@Override
	public void aplicaIntervalo(int intervalo) throws RemoteException {
		this.intervalo = intervalo;	
		System.out.println("****Intervalo de Consulta do Consumidor Atualizado: "+this.intervalo);
	}

	protected int getIntervalo(){
		return this.intervalo;
	}
	
}
