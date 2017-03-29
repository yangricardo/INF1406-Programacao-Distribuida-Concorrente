package wrongImplementation;

public class Fork {

	private int id;
	private boolean isBeingUsed;
	private int philId;
	
	public Fork(int id){
		//Construtor gera um fork com id e estado de não uso / false
		this.id = id;
		isBeingUsed = false;
	}
	
	public boolean isBeingUsed(){
		if(isBeingUsed == true){
			System.out.println("[Garfo "+this.id+"] esta com o filosofo "+ philId);
		}
		return isBeingUsed;
	}
	
	public void takeFork(int philId){
		isBeingUsed = true;
		this.philId = philId;
	}
	
	public void putFork(){
		isBeingUsed = false;
		this.philId = -1;
	}
	
	public int getId(){
		return id;
	}
}
