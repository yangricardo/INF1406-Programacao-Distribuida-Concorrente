package contracts;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

public interface Callback<T> extends Callable<T> {
	void entregaResultado(Resultado<T> resultado) throws RemoteException;
}
