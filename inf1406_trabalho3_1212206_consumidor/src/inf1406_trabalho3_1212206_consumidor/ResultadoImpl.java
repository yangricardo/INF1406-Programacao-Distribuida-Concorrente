package inf1406_trabalho3_1212206_consumidor;

import contracts.Resultado;

public class ResultadoImpl implements Resultado {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Double resultado;

	public Double getResultado() {
		return resultado;
	}

	public void setResultado(Double resultado) {
		this.resultado = resultado;
	}
	
}
