import java.util.concurrent.locks.*;


public class Chopstick {
	
	private boolean inuse;
	private ReentrantLock chopmutex = new ReentrantLock();
		
	public Chopstick() 
	{
	   inuse = false;
	}
	
	public void useChopstick()
	{
		chopmutex.lock();
		inuse = true;	
		chopmutex.unlock();				
	}
	
	public void dropChopstick()
	{
		chopmutex.lock();
		inuse = false;
		chopmutex.unlock();		
	}
	
	public boolean ChopstickBeingUsed()
	{
		boolean isInUse;
		chopmutex.lock();
		isInUse = inuse;
		chopmutex.unlock();
		return isInUse;		
	}	
}
