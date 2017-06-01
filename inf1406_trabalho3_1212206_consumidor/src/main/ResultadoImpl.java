package main;

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

	@Override
	public Double getResultado() {
		return resultado;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public void print() {
		System.out.println("["+this.line+","+this.column+"]: "+this.resultado);		
	}

	@Override
	public void setResultado(Double resultado) {
		this.resultado = resultado;		
	}

}
