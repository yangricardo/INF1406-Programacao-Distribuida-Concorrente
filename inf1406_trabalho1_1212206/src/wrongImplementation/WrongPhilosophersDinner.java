package wrongImplementation;

public class WrongPhilosophersDinner {
	static Fork[] forks =  new Fork[5];
	static PhilosopherStatus[] philStates = new PhilosopherStatus[5];
	
	public static void main(String[] args) {
		//LogControl.createFile("wrongPhilosophersDinnerLog");
		for(int i = 0; i < 5; i++){
			forks[i] = new Fork(i);
			philStates[i] = PhilosopherStatus.STARTED;
		}
		for(int i = 0; i < 5; i++){
			Thread thread = new Thread(new Philosopher(i, forks,philStates));
			thread.start();
		}
		
	}

}
