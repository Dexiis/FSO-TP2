
import java.util.ArrayList;

public class Buffer {

	private ArrayList<Movement> buffer;
	private Record record;
	private GUI gui;

	public Buffer(GUI gui, Record record) {
		this.buffer = new ArrayList<>();
		this.gui = gui;
		this.record = record;
	}

	public synchronized void put(Movement movement) {
		buffer.add(movement);
		notifyAll();
		if (true && gui instanceof RandomMovementsGUI) {
			record.recordMovement(movement);
		}
	}

	public synchronized void higherPriorityPut(Movement movement) {
		buffer.add(0, movement);
		notifyAll();
		if (record.isRecording() && gui instanceof RandomMovementsGUI) {
			record.recordMovement(movement);
		}
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