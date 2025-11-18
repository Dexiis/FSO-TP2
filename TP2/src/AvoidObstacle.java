import java.util.Random;

public class AvoidObstacle implements Runnable {

	private StateEnum STATE = StateEnum.IDLE;
	
	private static final int MOVEMENT_NUMBER = 3;
	
	private RobotController robotController;
	private BufferManager bufferManager;
	private ILogger logger;
	private RobotLegoEV3 robot;
	private long waitingTime = 0;
	
	private Random random = new Random();
	
	private Movement[] movementList = new Movement[MOVEMENT_NUMBER];
	
	public AvoidObstacle(RobotLegoEV3 robot, ILogger logger, RobotController robotController, BufferManager bufferManager) {
		this.robotController = robotController;
		this.bufferManager = bufferManager;
		this.logger = logger;
		this.robot = robot;
	}
	
	@Override
	public void run() {
		while(true) {
			switch(STATE) {
			case IDLE:
				synchronized(this) {
					// Ver de 50 em 50 ms (sem thread sleep)
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					/* MODO ROBO */
					if(robotController.robotOn && robotController.obstacleFound())
						STATE = StateEnum.SEND;
					
					/* MODO TESTE 
					if(robotController.obstacleFound())
						STATE = StateEnum.GENERATE;
					*/
					
				}
				break;
			case GENERATE:
				waitingTime = 0;
				
				movementList[0] = new Movement(this.robot, this.logger, MovementEnum.STOP);
				movementList[1] = new Movement(this.robot, this.logger, MovementEnum.BACKWARDS, 20);
				
				while(true) {
					MovementEnum[] movement = MovementEnum.values();
					int direction = random.nextInt(movement.length);
					
					if(movement[direction] == MovementEnum.LEFT || movement[direction] == MovementEnum.RIGHT) {
						movementList[2] = new Movement(this.robot, this.logger, movement[direction], 0, 90);
						break;
					}
				}
				
				for(Movement movement : movementList)
					waitingTime += movement.getTime();
				
				// PROBLEMA DE EXCLUSAO MUTUA (em teste não voltou a aparecer)
				
			case SEND:
				bufferManager.acquire();
				for(int i = MOVEMENT_NUMBER - 1; i >= 0; i--) {
					robotController.putBufferHigherPriority(movementList[i]);
				}	
				bufferManager.release();
				STATE = StateEnum.WAIT;
				break;
			case WAIT:
				try {
					Thread.sleep(waitingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				STATE = StateEnum.IDLE;
				
				break;
			default:
				break;
			}
		}
	}
	
}
