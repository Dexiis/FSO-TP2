public class RobotController implements Runnable {

	private final Data data = new Data();
	private final RobotLegoEV3 robot = new RobotLegoEV3();
	private final RandomMovements randomMovements;
	private final AvoidObstacle avoidObstacle;
	private final RobotManager robotManager;
	private final BufferManager bufferManager = new BufferManager();
	private final Buffer buffer = new Buffer();
	private final Thread randomMovementsThread;
	private final Thread avoidObstacleThread;

	public boolean robotOn = false;

	private ILogger logger;

	private Movement movement;
	private StateEnum bufferState = StateEnum.IDLE;

	private long waitingTime;

	public RobotController(ILogger logger) {
		this.logger = logger;
		this.robotManager = new RobotManager();
		this.randomMovements = new RandomMovements(robot, logger, this, bufferManager);
		this.avoidObstacle = new AvoidObstacle(robot, logger, this, bufferManager, robotManager);
		this.randomMovementsThread = new Thread(randomMovements);
		this.randomMovementsThread.start();
		this.avoidObstacleThread = new Thread(avoidObstacle);
		this.avoidObstacleThread.start();
	}

	public void run() {
		while (true) {
			switch (bufferState) {
			case IDLE:
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}
				bufferState = StateEnum.EXECUTE;
				break;
			case EXECUTE:
				bufferManager.acquire();
				movement = buffer.get();
				bufferManager.release();

				robotManager.acquire();
				movement.doMovement();
				robotManager.release();

				waitingTime = movement.getTime();
				bufferState = StateEnum.WAIT;
				break;
			case WAIT:
				try {
					Thread.sleep(waitingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (buffer.isEmpty()) {
					bufferState = StateEnum.IDLE;
					stopMovementSync();
				} else {
					bufferState = StateEnum.EXECUTE;
				}
				break;
			default:
				break;
			}
		}
	}

	private void log(String message) {
		if (logger != null) {
			logger.logMessage(message);
		}
	}

	public void updateRadius(int radius) {
		data.setRadius(radius);
	}

	public void updateAngle(int angle) {
		data.setAngle(angle);
	}

	public void updateDistance(int distance) {
		data.setDistance(distance);
	}

	public void updateActionNumber(int actionNumber) {
		data.setActionNumber(actionNumber);
	}

	public void turnOnRobot(String name) {
		robot.OpenEV3(name);
		this.robotOn = true;
	}

	public void turnOffRobot() {
		robot.CloseEV3();
	}

	public synchronized void bufferMoveForward() {
		bufferManager.acquire();
		buffer.put(new ForwardMovement(data.getDistance(), robot, logger));
		bufferManager.release();
		notify();
	}

	public synchronized void bufferMoveBackwards() {
		bufferManager.acquire();
		buffer.put(new BackwardsMovement(data.getDistance(), robot, logger));
		bufferManager.release();
		notify();
	}

	public synchronized void bufferMoveRightCurve() {
		bufferManager.acquire();
		buffer.put(new RightMovement(data.getRadius(), data.getAngle(), robot, logger));
		bufferManager.release();
		notify();
	}

	public synchronized void bufferMoveLeftCurve() {
		bufferManager.acquire();
		buffer.put(new LeftMovement(data.getRadius(), data.getAngle(), robot, logger));
		bufferManager.release();
		notify();
	}

	public synchronized void bufferStopMovement() {
		bufferManager.acquire();
		buffer.put(new StopMovement(robot, logger));
		bufferManager.release();
		notify();
	}

	public synchronized void putBuffer(Movement movement) {
		buffer.put(movement);
		notify();
	}

	public synchronized void putBufferHigherPriority(Movement movement) {
		buffer.higherPriorityPut(movement);
		notify();
	}

	public void stopMovement() {
		bufferManager.acquire();
		putBufferHigherPriority(new StopMovement(robot, logger));
		bufferManager.release();
		this.waitingTime = 0;
		log("O robô parou por completo.\n");
	}

	public void stopMovementSync() {
		robot.Parar(false);
	}

	public void squareMovement() {
		bufferMoveForward();
		bufferMoveLeftCurve();
		bufferMoveForward();
		bufferMoveLeftCurve();
		bufferMoveForward();
		bufferMoveLeftCurve();
		bufferMoveForward();
		bufferMoveLeftCurve();
	}

	public synchronized boolean obstacleFound() {
		return robot.SensorToque(robot.S_1) == 1;
	}

	public void startRandomMovements() {
		randomMovements.setActionNumber(data.getActionNumber());
		randomMovements.setWorking(true);
	}

	public void stopRandomMovements() {
		randomMovements.setWorking(false);
	}

	public void clearBuffer() {
		buffer.clearBuffer();
	}
}