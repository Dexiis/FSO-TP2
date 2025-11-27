
import java.util.ArrayList;

public class Buffer {

	private ArrayList<Movement> buffer;

	public Buffer() {
		this.buffer = new ArrayList<>();
	}

	public synchronized void put(Movement movement) {
		buffer.add(movement);
		notifyAll();
	}

	public synchronized void higherPriorityPut(Movement movement) {
		buffer.add(0, movement);
		notifyAll();
	}

	public synchronized Movement get() {
		while (isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return buffer.remove(0);
	}

	public boolean isEmpty() {
		return buffer.isEmpty();
	}

	public void clearBuffer() {
		buffer.clear();
	}
}