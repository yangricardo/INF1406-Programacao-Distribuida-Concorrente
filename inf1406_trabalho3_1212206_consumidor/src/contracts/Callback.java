package contracts;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

public interface Callback<T> extends Serializable,Callable<T> {
	void entregaResultado(Resultado<T> resultado) throws RemoteException;
}
