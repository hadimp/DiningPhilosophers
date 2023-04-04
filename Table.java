import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.*;

public class Table {
	private static int numSeats = 5;
	private Chair[] chairs;
	public char lastPhilosopherID;
	public int  nextSeat = 0;
	public boolean DeadLocked;
	public Semaphore movePhilosopher;
	private ReentrantLock tablemutex = new ReentrantLock();

	public Table() 
	{
		this.chairs = new Chair[numSeats];
		for(int i = 0; i < numSeats; i++) {
			chairs[i] = new Chair();
		}
		this.DeadLocked = false;
		this.movePhilosopher = new Semaphore(1);
	}
	
	public boolean isDeadlocked()
	{	tablemutex.lock();
		if(!DeadLocked){
			for(int i = 0; i < numSeats; i++) {
				if(getChair(i).phil == null) {
					tablemutex.unlock();
					return false;
				}
				else if(!getChair(i).isWaiting()) {
					tablemutex.unlock();
					return false;
				}
			}
			DeadLocked = true;
		}
		tablemutex.unlock();
		return true;
	}
	
	public int getNumChairs()
	{
		return numSeats;
	}
	
	public Chair getChair(int chairnum)
	{
		return chairs[chairnum];
	}
	
	public void addPhilosopher(Philosopher p)
	{
		tablemutex.lock();
		if(nextSeat < numSeats) {
			chairs[nextSeat++].addPhilosopher(p);
		}
		tablemutex.unlock();
	}
	
	public char getLastPhilosopherID()
	{
		lastPhilosopherID = chairs[numSeats-1].getPhilosopher().ID;
		return lastPhilosopherID;
	}
	
	// This method should only be called when deadlock has been detected
	public void movePhilosopher(Table otherTable)
	{	
		tablemutex.lock();
		if(movePhilosopher.tryAcquire()) {
			int thisChair = getRandomNumber(0, numSeats-1);
			Philosopher movingPhilosopher = getChair(thisChair).getPhilosopher();
			if(movingPhilosopher.hasLeftChopstick) {movingPhilosopher.leftChop.dropChopstick();}
			if(movingPhilosopher.hasRightChopstick) {movingPhilosopher.rightChop.dropChopstick();}
			otherTable.addPhilosopher(movingPhilosopher);
		}
		tablemutex.unlock();
	}
	
	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
	
}
