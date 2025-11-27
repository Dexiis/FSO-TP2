
public class RobotManager {

	private boolean inUse = false;

	public RobotManager() {

	}

	public synchronized void acquire() {
		while (inUse) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		inUse = true;
	}

	public synchronized void release() {
		inUse = false;
		notify();
	}

}
