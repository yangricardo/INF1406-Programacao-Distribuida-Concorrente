package wrongImplementation;

import common.Fork;
import common.PhilosopherStatus;

public class WrongPhilosophersDinner {
	final static int maxPhilosophers = 5;
	static Fork[] forks =  new Fork[maxPhilosophers];
	static PhilosopherStatus[] philStates = new PhilosopherStatus[maxPhilosophers];
	
	public static void main(String[] args) {
		for(int i = 0; i < maxPhilosophers; i++){
			forks[i] = new Fork(i);
			philStates[i] = PhilosopherStatus.STARTED;
		}
		for(int i = 0; i < maxPhilosophers; i++){
			Thread thread = new Thread(new Philosopher(i, maxPhilosophers ,forks,philStates));
			thread.start();
		}		
	}

}
