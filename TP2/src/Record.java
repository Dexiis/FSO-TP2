import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Record {
	
	private volatile boolean recording = false;
	
	private ArrayList<Movement> movementList = new ArrayList<>();
	
	private OutputStream os;
	
	public Record() {
		
	}
	
	public synchronized boolean isRecording() {
		return this.recording;
	}
	
	public void recordMovement(Movement movement) {
		if(isRecording()) {
			movementList.add(movement);
			
			try {
				// Write the actual movement
				os.write(55);
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void startRecording() {
		if(!isRecording()) {
			recording = true;
			
			try {
				File file = new File("recorded/Recording.txt");
				os = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopRecording() {
		if(isRecording()) {
			recording = false;
		
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			movementList.clear();
		}
	}
	
}
