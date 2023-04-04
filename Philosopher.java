import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Philosopher extends Thread {
	public Clock SimulationClock;
	public Lock lock;
	public Condition clockTicked;
	public Chopstick leftChop;
	public Chopstick rightChop;
	public boolean isWaiting;
	public char ID;
	public boolean hasLeftChopstick;
	public boolean hasRightChopstick;
	
	public Philosopher(char ID, Chopstick leftChop, Chopstick rightChop) {
		this.ID = ID;
		this.leftChop = leftChop;
		this.rightChop = rightChop;
		this.isWaiting = false;
		this.hasLeftChopstick = false;
		this.hasRightChopstick = false;
	}

	public void run() {
		lock.lock();
		try {
			while(true) {
				int StartThinking = SimulationClock.getTick();
				int ThinkingTime = getRandomNumber(0, 10);
				
				while(SimulationClock.getTick() - StartThinking < ThinkingTime) {
					clockTicked.await();
				}
				
				while(leftChop.ChopstickBeingUsed()) {
					isWaiting = true;
					clockTicked.await();
				}
				leftChop.useChopstick();
				hasLeftChopstick = true;
				isWaiting = false;
				
				int StartWaiting = SimulationClock.getTick();
				
				while(SimulationClock.getTick() - StartWaiting < 4) {
					clockTicked.await();
				}
				
				while(rightChop.ChopstickBeingUsed()) {
					isWaiting = true;
					clockTicked.await();
				}
				rightChop.useChopstick();
				hasRightChopstick = true;
				isWaiting = false;

				
				int StartEating = SimulationClock.getTick();
				int EatingTime = getRandomNumber(0, 5);
				while(SimulationClock.getTick() - StartEating < EatingTime) {
					clockTicked.await();
				}
				
				leftChop.dropChopstick();
				rightChop.dropChopstick();
				
			}
			
		} catch (InterruptedException e) {

		}
		finally {
			lock.unlock();
		}
	}

	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
}
