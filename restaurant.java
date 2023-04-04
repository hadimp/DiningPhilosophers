import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class restaurant implements Runnable{
	public Clock SimulationClock = new Clock();
	public Lock QClockLock = new ReentrantLock();
	public Condition clockTicked = QClockLock.newCondition();
	public int numPhilosophers = 25;
	public int numTables = 6;
	public int numSeats = 5;
	public Table [] tables = new Table[numTables];
	public Philosopher [] philosophers = new Philosopher[numPhilosophers];
	public Chopstick [][] chopsticks = new Chopstick[numTables][numSeats];
	
	public void run(){
		
		for(int k = 0; k < numTables; k++){
			tables[k] = new Table();
			for(int u = 0; u < numSeats; u++){
				chopsticks[k][u] = new Chopstick();
			}
		}
		int i = 0;
		int left = 0;
		int right = numSeats - 1;
	    for(char ID = 'A'; ID < 'Z'; ID++) {
			int tableNum = i / numSeats;
			philosophers[i] = new Philosopher(ID, chopsticks[tableNum][left++], chopsticks[tableNum][right++]);
			philosophers[i].lock = QClockLock;
			philosophers[i].SimulationClock = SimulationClock;
			philosophers[i].clockTicked = clockTicked;
			tables[tableNum].addPhilosopher(philosophers[i++]);
			if(right == numSeats) {right = 0;}
			if(left == numSeats) {left = 0;}
		}
	    tables[numTables - 1] = new Table();
	    
	    for(int j = 0; j < numPhilosophers; j++) {
	    	philosophers[j].start();
	    }
		while(!tables[numTables-1].isDeadlocked()) {
			try {
				Thread.sleep(50);
				QClockLock.lockInterruptibly();
				SimulationClock.tick();
				clockTicked.signalAll();
				for(int t = 0; t < numTables-1; t++) {
					if(tables[t].isDeadlocked()) {
						tables[t].movePhilosopher(tables[numTables-1]);
					}
				}
				
			} catch (InterruptedException e) {
			}
			finally {
				QClockLock.unlock();
			}

		}
		System.out.println("The ID of last philosopher to move to table 6 is:");
		System.out.println(tables[numTables-1].getLastPhilosopherID());
		System.out.println();
		System.out.println("Running time before the sixth table enters a state of deadlock is:");
		System.out.println(SimulationClock.getTick());

	}
}
