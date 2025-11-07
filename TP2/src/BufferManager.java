public class BufferManager {

	private boolean inUse = false;

	public BufferManager() {
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
		notifyAll();
	}
}
