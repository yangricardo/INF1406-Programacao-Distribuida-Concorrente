package common;

public class Fork {

	// a classe NÃO É THREAD-SAFE
	final private int id;
	private boolean isBeingUsed;
	private int philId;

	public Fork(int id) {
		// Construtor gera um fork com id e estado de não uso / false
		this.id = id;
		this.isBeingUsed = false;
	}

	public boolean isBeingUsed() {
		// se o garfo esta sendo usado e não for pelo filosofo que chama esse
		// metodo exibe a mensagem
		if (isBeingUsed == true) {
			System.out.println("[Garfo " + this.id + "] esta com o filosofo " + philId);
		}
		return isBeingUsed;
	}

	public boolean isBeingUsedBy(int philId) {
		// se o garfo esta sendo usado e não for pelo filosofo que chama esse
		// metodo exibe a mensagem
		return (isBeingUsed && (this.philId == philId));
	}
	
	public void takeFork(int philId) {
		isBeingUsed = true;
		this.philId = philId;
	}

	public void putFork() {
		isBeingUsed = false;
		this.philId = -1;
	}

	public int getId() {
		return id;
	}
}
