
public class Chair {
	public Philosopher phil;
	
	public Chair() 
	{
		phil = null;
	}
	
	public boolean getOccupied()
	{
		return (phil != null);
	}
		
	public void addPhilosopher(Philosopher p)
	{
		phil = p;
	}
	
	public Philosopher getPhilosopher()
	{
		return phil;
	}
	
	public void removePhilosopher()
	{
		phil = null;
	}
	
	public boolean isWaiting() 
	{
		return phil.isWaiting;
	}
}

