package main;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

import contracts.Callback;
import contracts.Resultado;

public class CallbackImpl implements Callback,Serializable {

	private static final long serialVersionUID = 1L;
	private Resultado resultado;
	private Semaphore sema;
	
	public CallbackImpl(Resultado resultado, Semaphore sema) {
		super();
		this.resultado = null;
		this.resultado = resultado;
		this.sema = sema;
	}

	@Override
	public void entregaResultado(Resultado resultado) throws RemoteException {
		this.resultado = resultado;
		this.sema.release();
	}

	@Override
	public Resultado getResultado() throws RemoteException{
		return this.resultado;
	}
	
}
