package main;

import java.rmi.RemoteException;

import contracts.Configuracao;

public class ConfiguracaoImpl implements Configuracao{

	private int intervalo;
	
	public ConfiguracaoImpl() {
		super();
		this.intervalo = 1000;
	}

	@Override
	public void aplicaIntervalo(int intervalo) throws RemoteException {
		this.intervalo = intervalo;	
	}

	protected int getIntervalo(){
		return this.intervalo;
	}
	
}
