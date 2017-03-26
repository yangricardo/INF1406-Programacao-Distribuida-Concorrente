package wrongImplementation;

import fileLog.LogControl;

public class Philosopher implements Runnable {

	private int id;
	private static Fork[] forks;
	private static PhilosopherStatus[] philStates;

	public Philosopher(int id, Fork[] forks, PhilosopherStatus[] philStates) {
		this.id = id;
		Philosopher.forks = forks;
		Philosopher.philStates = philStates;
	}

	private void think() throws InterruptedException {
		philStates[this.id] = PhilosopherStatus.THINKING;
		printMessage(this.id, "vai pensar");
		Thread.sleep(1000);//pensando 1 segundo
		philStates[this.id] = PhilosopherStatus.HUNGRY;
		printMessage(this.id, "terminou de pensar");
	}

	private void eat() throws InterruptedException {
		// TODO
		if (philStates[this.id] == PhilosopherStatus.HUNGRY && philStates[(this.id + 4) % 5] != PhilosopherStatus.EATING
				&& philStates[(this.id + 1) % 5] != PhilosopherStatus.EATING) {
			printMessage(this.id, "vai comer");
			philStates[this.id] = PhilosopherStatus.EATING;
			Thread.sleep(1000);//come por 1 segundo
			philStates[this.id] = PhilosopherStatus.NOT_HUNGRY;
			printMessage(this.id, "terminou de comer");
		}
	}

	private boolean takeFork(int forkId) {
		// TODO Auto-generated method stub
		String leftOrRight = (forkId == this.id ? " 'a esquerda' " : " 'a direita' ");
		
		printMessage(this.id, "vai tentar pegar o garfo " + forkId + leftOrRight);
		
		if (!forks[forkId].isBeingUsed()) {
			//se o garfo não estiver sendo usado o filosofo vai tentar pegar
			printMessage(this.id, "pegou o garfo " + forkId + leftOrRight);
			forks[forkId].takeFork(this.id);
			return true;
		} else {
			//senão continua com fome
			printMessage(this.id, "não pegou o garfo " + forkId + leftOrRight);
			philStates[this.id] = PhilosopherStatus.HUNGRY;
			return false;
		}
	}

	private void putFork(int forkId) {
		// TODO Auto-generated method stub
		String leftOrRight = (forkId == this.id ? " 'a esquerda' " : " 'a direita' ");
		printMessage(this.id, "devolveu o garfo " + forkId + leftOrRight);
		forks[forkId].putFork();
	}

	private void putForks() {
		putFork(this.id);
		putFork((this.id + 1) % 5);
	}

	private void printMessage(int id, String message) {
		String logMessage = "[Filosofo " + id + " - " + philStates[id] + "] " + message;
		//LogControl.writeMessage("wrongPhilosophersDinnerLog", logMessage);
		System.out.println(logMessage);
	}


	@Override
	public void run() {
		printMessage(this.id, "começou");
		while (true) {
			try {
				think();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			takeFork(this.id);
			takeFork((this.id + 1) % 5);
			try {
				eat();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			putFork(this.id);
			putFork((this.id + 1) % 5);

		}

	}
}
