package rightImplementation;

public class Philosopher implements Runnable {

	private int id;
	private static Fork[] forks;
	private static PhilosopherStatus[] philStates;
	private static Object LOCK;

	public Philosopher(int id, Fork[] forks, PhilosopherStatus[] philStates, Object lock) {
		this.id = id;
		Philosopher.forks = forks;
		Philosopher.philStates = philStates;
		Philosopher.LOCK = lock;
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

	private boolean tryTakeForks() {
		// faz chamada para pegar o garfo a esquerda e a direita
		return takeFork(this.id) && takeFork((this.id + 1) % 5);
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
		System.out.println("[Filosofo " + id + " - " + philStates[id] + "] " + message);
	}

	@Override
	public void run() {
		printMessage(this.id, "começou");
		while (true) {
			try {
				//Se o filosofo não estiver com fome vai pensar
				if(philStates[this.id] != PhilosopherStatus.HUNGRY)
					think();
				//senão vai tentar comer
				synchronized (LOCK) {
					if (tryTakeForks()){
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
