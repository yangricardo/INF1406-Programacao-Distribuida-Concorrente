package contracts;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Resultado extends Serializable {
	void setResultCell(Double resultCell) throws RemoteException;
	Double getResultCell() throws RemoteException;
	int getLine() throws RemoteException;
	int getColumn() throws RemoteException;
}
