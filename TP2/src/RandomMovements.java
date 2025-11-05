import java.util.Random;

public class RandomMovements implements Runnable {

	private StateEnum STATE = StateEnum.IDLE;
	private MovementEnum lastDirection = null;

	private volatile boolean working = false;

	private volatile int actionNumber = 0;
	private long waitingTime = 0;
	private long timeStamp = 0;

	private RobotController robotController;
	private Random random = new Random();

	public RandomMovements(RobotController robotController) {
		this.robotController = robotController;
	}

	public void run() {
		while (true) {
			switch (STATE) {
			case IDLE:
				if (this.working)
					STATE = StateEnum.EXECUTE;
				break;

			case EXECUTE:
				waitingTime = 0;

				for (int i = 0; i < this.actionNumber; i++) {
					MovementEnum[] movement = MovementEnum.values();
					int direction = random.nextInt(3);

					robotController.updateData(random.nextInt(20) + 10, random.nextInt(70) + 20,
							random.nextInt(40) + 10);

					if (lastDirection == movement[direction]) {
						i--;
						continue;
					}

					switch (movement[direction]) {
					case FORWARD:
						robotController.moveForward();
						waitingTime += robotController.getDelayStraightLine();
						robotController.bufferStopMovement();
						waitingTime += 100;
						break;
					case RIGHT:
						robotController.moveRightCurve();
						waitingTime += robotController.getDelayCurve();
						robotController.bufferStopMovement();
						waitingTime += 100;
						break;
					case LEFT:
						robotController.moveLeftCurve();
						waitingTime += robotController.getDelayCurve();
						robotController.bufferStopMovement();
						waitingTime += 100;
						break;
					default:
						i--;
						continue;
					}

					lastDirection = movement[direction];
				}
				STATE = StateEnum.WAIT;
				timeStamp = System.currentTimeMillis();
				break;

			case WAIT:
				if (System.currentTimeMillis() - timeStamp >= waitingTime + 4000) {
					if (this.working)
						STATE = StateEnum.EXECUTE;
					else
						STATE = StateEnum.IDLE;
				}
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
