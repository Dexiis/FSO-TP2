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
	
	public boolean isRecording() {
		return this.recording;
	}
	
	public synchronized void recordMovement(Movement movement) {
		System.out.println(isRecording());
		if(isRecording()) {
			movementList.add(movement);
			
			try {
				os.write(22);
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public synchronized void startRecording() {
		if(!this.recording) {
			System.out.println("a");
			this.recording = true;
			
			try {
				File file = new File("recorded/Recording.txt");
				os = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void stopRecording() {
		if(this.recording) {
			this.recording = false;
		
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			movementList.clear();
		}
	}
	
}
