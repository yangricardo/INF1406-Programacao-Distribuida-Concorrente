package contracts;

import java.io.Serializable;

public interface Resultado extends Serializable {
	public void setResultado(Double resultado);
	public Double getResultado();
	public int getLine();
	public int getColumn();
	public void print();
}
