package rightImplementation;

import common.PhilosopherStatus;
import common.Fork;

public class Philosopher extends Thread {

	private int id;
	private static Fork[] forks;
	private static PhilosopherStatus[] philStates;
	private static Object LOCK;
	private static int maxPhilosophers;
	private int leftFork;
	private int rightFork;

	public Philosopher(int id, int maxphilosophers, Fork[] forks, PhilosopherStatus[] philStates, Object lock) {
		this.id = id;
		Philosopher.forks = forks;
		Philosopher.philStates = philStates;
		Philosopher.LOCK = lock;
		Philosopher.maxPhilosophers = maxphilosophers;
		this.leftFork = this.id;
		this.rightFork = (this.id + 1) % maxPhilosophers;
	}

	private void think() throws InterruptedException {
		philStates[this.id] = PhilosopherStatus.THINKING;
		printMessage(this.id, "vai pensar");
		Philosopher.sleep(1000);// pensando 1 segundo
		philStates[this.id] = PhilosopherStatus.HUNGRY;
		printMessage(this.id, "terminou de pensar");
	}

	private void eat() throws InterruptedException {
		// TODO
		if (philStates[this.id] == PhilosopherStatus.HUNGRY && isOwningBothForks()) {
			printMessage(this.id, "vai comer");
			philStates[this.id] = PhilosopherStatus.EATING;
			Philosopher.sleep(1000);// come por 1 segundo
			philStates[this.id] = PhilosopherStatus.NOT_HUNGRY;
			printMessage(this.id, "terminou de comer");
		}
	}

	private boolean isOwningBothForks() {
		return forks[leftFork].isBeingUsedBy(this.id) && forks[rightFork].isBeingUsedBy(this.id);
	}

	private String isLeftOrRightFork(int forkId) {
		return (forkId == leftFork ? " 'a esquerda' " : " 'a direita' ");
	}

	private boolean takeFork(int forkId) {
		// TODO Auto-generated method stub
		printMessage(this.id, "vai tentar pegar o garfo " + forkId + isLeftOrRightFork(forkId));
		if (!forks[forkId].isBeingUsed()) {
			// se o garfo não estiver sendo usado o filosofo vai tentar pegar
			printMessage(this.id, "pegou o garfo " + forkId + isLeftOrRightFork(forkId));
			forks[forkId].takeFork(this.id);
			return true;
		} else {
			// senão continua com fome
			philStates[this.id] = PhilosopherStatus.HUNGRY;
			return false;
		}
	}

	private boolean tryTakeForks() {
		// faz chamada para pegar o garfo a esquerda e a direita
		return takeFork(leftFork) && takeFork(rightFork);
	}

	private void putFork(int forkId) {
		// TODO Auto-generated method stub
		printMessage(this.id, "devolveu o garfo " + forkId + isLeftOrRightFork(forkId));
		forks[forkId].putFork();
	}

	private void putForks() {
		putFork(leftFork);
		putFork(rightFork);
	}

	private void printMessage(int id, String message) {
		System.out.println("[Filosofo " + id + " - " + philStates[id] + "] " + message);
	}

	@Override
	public void run() {
		printMessage(this.id, "começou");
		while (true) {
			try {
				// Se o filosofo não estiver com fome vai pensar
				if (philStates[this.id] != PhilosopherStatus.HUNGRY)
					think();
				// senão vai tentar comer
				synchronized (LOCK) {
					if(tryTakeForks()){
						eat();
						putForks();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
