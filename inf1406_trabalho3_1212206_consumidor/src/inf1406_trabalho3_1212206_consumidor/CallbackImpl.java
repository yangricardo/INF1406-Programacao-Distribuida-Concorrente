package inf1406_trabalho3_1212206_consumidor;

import java.io.Serializable;
import java.rmi.RemoteException;

import contracts.Callback;
import contracts.Resultado;

public class CallbackImpl implements Callback,Serializable {

	private static final long serialVersionUID = 1L;
	private Resultado resultado;
	
	public CallbackImpl() {
		super();
		this.resultado = null;
	}

	@Override
	public void entregaResultado(Resultado resultado) throws RemoteException {
		this.resultado = resultado;
	}

	public Resultado getResultado(){
		return this.resultado;
	}
	
}
