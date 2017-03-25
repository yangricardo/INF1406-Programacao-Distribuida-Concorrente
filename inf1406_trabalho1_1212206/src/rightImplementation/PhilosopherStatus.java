package rightImplementation;

public enum PhilosopherStatus {
	STARTED,THINKING,HUNGRY,EATING,NOT_HUNGRY
	//STARTED: estado inicial, quando a thread é criada
	//THINKING: ativado com o metodo Philosopher.think()
	//HUNGRY: ativado apos o sleep no Philosopher.think(), ou seja, apos o filosofo para de pensar 
	//EATING: ativado com o metodo Philosopher.eat()
	//NOT_HUNGRY: ativado apos o sleep no Philosopher.eat()
}
