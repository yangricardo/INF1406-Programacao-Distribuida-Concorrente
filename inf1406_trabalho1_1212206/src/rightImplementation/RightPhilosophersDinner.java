package rightImplementation;

public class RightPhilosophersDinner {
	
	//vetor de Forks/Garfos
	static Fork[] forks =  new Fork[5];
	//vetor do mapa de estados dos filosofos
	static PhilosopherStatus[] philStates = new PhilosopherStatus[5];
	final static Object LOCK = new Object();
	public static void main(String[] args) {
		for(int i = 0; i < 5; i++){
			//instancialização dos garfos e dos estados dos filosofos iniciais
			forks[i] = new Fork(i);
			philStates[i] = PhilosopherStatus.STARTED;
		}
		for(int i = 0; i < 5; i++){
			//cria as threads dos filosofos passando por referencia a identificação,
			//a região critica dos garfos e o mapa de estados dos filosofos 
			Thread thread = new Thread(new Philosopher(i, forks,philStates,LOCK));
			thread.start();
		}
		
	}

}
