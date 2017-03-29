package rightImplementation;

import common.PhilosopherStatus;
import common.Fork;

public class RightPhilosophersDinner {
	
	final static int maxPhilosophers = 5;
	//vetor de Forks/Garfos
	static Fork[] forks =  new Fork[maxPhilosophers];
	//vetor do mapa de estados dos filosofos
	static PhilosopherStatus[] philStates = new PhilosopherStatus[maxPhilosophers];
	final static Object LOCK = new Object();
	public static void main(String[] args) {
		for(int i = 0; i < maxPhilosophers; i++){
			//instancialização dos garfos e dos estados dos filosofos iniciais
			forks[i] = new Fork(i);
			philStates[i] = PhilosopherStatus.STARTED;
		}
		for(int i = 0; i < maxPhilosophers; i++){
			//cria as threads dos filosofos passando por referencia a identificação,
			//a região critica dos garfos e o mapa de estados dos filosofos 
			Thread thread = new Thread(new Philosopher(i,maxPhilosophers,forks,philStates,LOCK));
			thread.start();
		}
		
	}

}
