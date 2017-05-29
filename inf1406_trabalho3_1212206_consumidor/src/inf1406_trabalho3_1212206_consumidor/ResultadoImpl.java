package inf1406_trabalho3_1212206_consumidor;

import java.rmi.RemoteException;

public class ResultadoImpl implements contracts.Resultado {

	private static final long serialVersionUID = 1L;

	private int line;
	private int column;
	private Double resultcell;
	public ResultadoImpl(int line, int column) {
		super();
		this.line = line;
		this.column = column;
	}
	@Override
	public void setResultCell(Double resultCell) throws RemoteException {
		this.resultcell = resultCell;
		
	}
	@Override
	public Double getResultCell() throws RemoteException {
		return this.resultcell;
	}
	@Override
	public int getLine() throws RemoteException {
		return this.line;
	}
	@Override
	public int getColumn() throws RemoteException {
		return this.column;
	}
	
}
