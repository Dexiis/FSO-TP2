public class RobotAccessManager {
	
	private boolean inUse = false;
	
	public RobotAccessManager() {
		
	}

	public synchronized void acquire() {
		while (inUse) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		inUse = true;
	}

	public synchronized void release() {
		inUse = false;
		notify();
	}
	
}
