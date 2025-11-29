
import java.util.Random;

public class AvoidObstacle implements Runnable {

	private StateEnum STATE = StateEnum.IDLE;

	private static final int MOVEMENT_NUMBER = 3;

	private Controller controller;
	private AccessManager bufferManager;
	private ILogger logger;
	private RobotLegoEV3 robot;
	private AccessManager robotManager;
	private long waitingTime = 0;

	private Random random = new Random();

	private Movement[] movementList = new Movement[MOVEMENT_NUMBER];

	public AvoidObstacle(RobotLegoEV3 robot, ILogger logger, Controller controller,
			AccessManager bufferManager, AccessManager robotManager) {
		this.robotManager = robotManager;
		this.controller = controller;
		this.bufferManager = bufferManager;
		this.logger = logger;
		this.robot = robot;
	}

	@Override
	public void run() {
		while (true) {
			switch (STATE) {
			case IDLE:
				synchronized (this) {
					try {
						wait(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					robotManager.acquire();
					if (controller.robotOn && controller.obstacleFound())
						STATE = StateEnum.GENERATE;
					robotManager.release();
				}
				break;
			case GENERATE:
				waitingTime = 0;

				movementList[0] = new StopMovement(this.robot, this.logger);
				movementList[1] = new BackwardsMovement(20, this.robot, this.logger);

				while (true) {
					MovementEnum[] movement = MovementEnum.values();
					int direction = random.nextInt(movement.length);

					if (movement[direction] == MovementEnum.LEFT) {
						movementList[2] = new LeftMovement(10, 90, this.robot, this.logger);
						break;
					} else if (movement[direction] == MovementEnum.RIGHT) {
						movementList[2] = new RightMovement(10, 90, this.robot, this.logger);
						break;
					}
				}

				for (Movement movement : movementList)
					waitingTime += movement.getTime();

				STATE = StateEnum.SEND;

			case SEND:
				bufferManager.acquire();
				for (int i = MOVEMENT_NUMBER - 1; i >= 0; i--)
					controller.putBufferHigherPriority(movementList[i]);
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
