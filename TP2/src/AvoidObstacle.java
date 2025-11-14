public class AvoidObstacle implements Runnable {

	private StateEnum STATE = StateEnum.IDLE;
	
	private static final int MOVEMENT_NUMBER = 3;
	
	private RobotController robotController;
	private BufferManager bufferManager;
	private ILogger logger;
	private RobotLegoEV3 robot;
	
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
					if(robotController.robotOn && robotController.obstacleFound()) {
						STATE = StateEnum.SEND;
					}
				}
				break;
			case GENERATE:
				movementList[0] = new Movement(this.robot, this.logger, MovementEnum.STOP);
				movementList[1] = new Movement(this.robot, this.logger, MovementEnum.BACKWARDS, 20);
				
				// Fazer random entre curvar direita e curvar esquerda
				
				// PROBLEMA DE EXCLUSAO MUTUA
				
			case SEND:
				bufferManager.acquire();
				for(int i = MOVEMENT_NUMBER - 1; i > 0; i--) {
					robotController.putBufferHigherPriority(movementList[i]);
				}
				bufferManager.release();
				STATE = StateEnum.WAIT;
				break;
			case WAIT:
				
				// Fazer o wait
				
				STATE = StateEnum.IDLE;
				
				break;
			default:
				break;
			}
		}
	}
	
}
