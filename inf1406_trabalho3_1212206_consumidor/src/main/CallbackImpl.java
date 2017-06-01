package main;

import java.io.Serializable;
import java.rmi.RemoteException;

import contracts.Callback;
import contracts.Resultado;

public class CallbackImpl implements Callback,Serializable {

	private static final long serialVersionUID = 1L;
	private Resultado resultado;
	
	public CallbackImpl(Resultado resultado) {
		super();
		this.resultado = null;
		this.resultado = resultado;
	}

	@Override
	public void entregaResultado(Resultado resultado) throws RemoteException {
		this.resultado = resultado;
	}

	@Override
	public Resultado getResultado() throws RemoteException{
		return this.resultado;
	}
	
}
