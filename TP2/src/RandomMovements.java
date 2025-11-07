import java.util.Random;

public class RandomMovements implements Runnable {

	private StateEnum STATE = StateEnum.IDLE;
	private MovementEnum lastDirection = null;

	private volatile boolean working = false;

	private volatile int actionNumber = 0;
	private long waitingTime = 0;
	private Random random = new Random();
	private RobotController robotController;
	private BufferManager bufferManager;
	private RobotLegoEV3 robot;
	private ILogger logger;
	private Movement[] movementList;

	public RandomMovements(RobotLegoEV3 robot, ILogger logger, RobotController robotController,
			BufferManager bufferManager) {
		this.robot = robot;
		this.logger = logger;
		this.robotController = robotController;
		this.bufferManager = bufferManager;
	}

	public void run() {
		while (true) {
			switch (STATE) {
			case IDLE:
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (working)
					STATE = StateEnum.GENERATE;
				break;

			case GENERATE:
				movementList = new Movement[actionNumber * 2];
				waitingTime = 0;

				for (int i = 0; i < this.actionNumber; i++) {
					MovementEnum[] movement = MovementEnum.values();
					int direction = random.nextInt(movement.length);

					if (lastDirection == movement[direction]) {
						i--;
						continue;
					}

					if (movement[direction] == MovementEnum.FORWARD)
						movementList[i * 2] = (new Movement(robot, this.logger, movement[direction],
								random.nextInt(40) + 10));
					else if (movement[direction] == MovementEnum.RIGHT || movement[direction] == MovementEnum.LEFT)
						movementList[i * 2] = (new Movement(robot, this.logger, movement[direction],
								random.nextInt(20) + 10, random.nextInt(70) + 20));
					else {
						i--;
						continue;
					}
					waitingTime += movementList[i * 2].getTime();

					movementList[i * 2 + 1] = (new Movement(robot, this.logger, MovementEnum.STOP));
					waitingTime += movementList[i * 2 + 1].getTime();

					lastDirection = movement[direction];
				}

				STATE = StateEnum.SEND;
				break;

			case SEND:
				System.out.println("a");
				bufferManager.acquire();
				for (int i = 0; i < this.actionNumber * 2; i++) {
					robotController.putBuffer(movementList[i]);
				}
				bufferManager.release();
				STATE = StateEnum.WAIT;
				break;

			case WAIT:
				try {
					Thread.sleep(waitingTime + 4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (this.working)
					STATE = StateEnum.GENERATE;
				else
					STATE = StateEnum.IDLE;
				break;
			default:
				break;
			}
		}
	}

	public synchronized void setWorking(boolean working) {
		this.working = working;
	}

	public synchronized void setActionNumber(int actionNumber) {
		this.actionNumber = actionNumber;
	}

}
