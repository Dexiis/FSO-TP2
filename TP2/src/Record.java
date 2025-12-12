import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Record {
	
	private volatile boolean recording = false;
	
	private OutputStream os;
	
	public synchronized boolean isRecording() {
		return this.recording;
	}
	
	public void recordMovement(Movement movement) {
		if(isRecording())
			try {
				os.write(movement.serializedMovement());	
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void startRecording() {
		if(!isRecording()) {
			recording = true;
			
			try {
				File file = new File("recorded/Recording.bin");
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
		}
	}
	
}
