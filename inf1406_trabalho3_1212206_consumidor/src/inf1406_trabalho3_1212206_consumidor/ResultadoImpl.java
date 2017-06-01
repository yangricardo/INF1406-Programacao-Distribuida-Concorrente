package inf1406_trabalho3_1212206_consumidor;

import contracts.Resultado;

public class ResultadoImpl implements Resultado {

	private static final long serialVersionUID = 1L;
	
	private Double resultado;
	private int line;
	private int column;

	public ResultadoImpl(int line,int column,Double resultado) {
		super();
		this.line = line;
		this.column = column;
		this.resultado = resultado;
	}

	public Double getResultado() {
		return resultado;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

}
