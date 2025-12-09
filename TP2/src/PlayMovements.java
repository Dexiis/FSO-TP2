import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class PlayMovements {
	
	private File file = null;
	private final ILogger logger;
	private final RobotLegoEV3 robot;
	private final Controller robotController;
	
	public PlayMovements(GUI gui) {
		this.logger = gui;
		this.robotController = gui.robotController;
		this.robot = this.robotController.getRobot();
	}
	
	public void loadFile(File file) {
		this.file = file;
	}

	public void playMovements() {
		try {
			InputStream input = new FileInputStream(file);
			
			int inputLength = input.available();
			int movementNumber = inputLength / Movement.FIXED_LENGTH;
			
			Movement[] movementList = new Movement[movementNumber];
			
			byte[] chunk = new byte[Movement.FIXED_LENGTH];
			int bytesRead = 0;
			int index = 0;
			
			while((bytesRead = input.read(chunk)) != -1) {
				
				Movement movement = null;
				
				char id = (char) chunk[0];
				int radiusOrDistance = ((chunk[1] & 0xFF) << 24) |
									   ((chunk[2] & 0xFF) << 16) | 
									   ((chunk[3] & 0xFF) << 8) |
									   (chunk[4] & 0xFF);
				int angle = ((chunk[5] & 0xFF) << 24) |
						    ((chunk[6] & 0xFF) << 16) | 
						    ((chunk[7] & 0xFF) << 8) |
						   	(chunk[8] & 0xFF); 
				
				switch(id) {
				case 's':
					movement = new StopMovement(robot, logger);
					break;
				case 'f':
					movement = new ForwardMovement(radiusOrDistance, robot, logger);
					break;
				case 'b':
					movement = new BackwardsMovement(radiusOrDistance, robot, logger);
					break;
				case 'r':
					movement = new RightMovement(radiusOrDistance, angle, robot, logger);
					break;
				case 'l':
					movement = new LeftMovement(radiusOrDistance, angle, robot, logger);
					break;
				}
				
				if(movement != null) {
					movementList[index] = movement;
				}
				
				index++;
			}
			
			robotController.bufferManagerAcquire();
			for(Movement currentMovement : movementList) {
				robotController.putBuffer(currentMovement);
			}
			robotController.bufferManagerRelease();
			
			input.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
