public class RobotController implements Runnable {

	private final Data data = new Data(0, 0, 0, 0, null);
	private final RobotLegoEV3 robot = new RobotLegoEV3();
	private final RandomMovements randomMovements;
	private final AvoidObstacle avoidObstacle;
	private final RobotManager robotManager;
	private final BufferManager bufferManager;
	private final Buffer buffer = new Buffer();
	private final Thread randomMovementsThread;
	private final Thread avoidObstacleThread;

	public boolean robotOn = false;

	/* TESTE */
	public boolean sensorTest = false;

	private ILogger logger;

	private Movement movement;
	private StateEnum bufferState = StateEnum.IDLE;

	private long waitingTime;

	public RobotController(BufferManager bufferManager, ILogger logger) {
		this.logger = logger;
		this.robotManager = new RobotManager();
		this.bufferManager = bufferManager;
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

	public void updateData(String radius, String angle, String distance, String name, String actionNumber) {
		data.setRadius(Integer.parseInt(radius));
		data.setAngle(Integer.parseInt(angle));
		data.setDistance(Integer.parseInt(distance));
		data.setName(name);
		data.setActionNumber(Integer.parseInt(actionNumber));
	}

	public void turnOnRobot() {
		robot.OpenEV3(data.getName());
		this.robotOn = true;
	}

	public void turnOffRobot() {
		// robot.CloseEV3();
	}

	public synchronized void bufferMoveForward() {
		buffer.put(new ForwardMovement(data.getDistance(), robot, logger));
		notify();
	}

	public synchronized void bufferMoveBackwards() {
		buffer.put(new BackwardsMovement(data.getDistance(), robot, logger));
		notify();
	}

	public synchronized void bufferMoveRightCurve() {
		buffer.put(new RightMovement(data.getRadius(), data.getAngle(), robot, logger));
		notify();
	}

	public synchronized void bufferMoveLeftCurve() {
		buffer.put(new LeftMovement(data.getRadius(), data.getAngle(), robot, logger));
		notify();
	}

	public synchronized void bufferStopMovement() {
		buffer.put(new StopMovement(robot, logger));
		notify();
	}

	public synchronized boolean obstacleFound() {
		/* MODO ROBO */
		return robot.SensorToque(robot.S_1) == 1;
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
		robot.Parar(true);
		this.waitingTime = 0;
		log("O robô parou.\n");
	}

	public void stopMovementSync() {
		robot.Parar(false);
	}

	public void squareMovement() {
		buffer.put(new ForwardMovement(20, robot, logger));
		buffer.put(new LeftMovement(0, 90, robot, logger));
		buffer.put(new ForwardMovement(20, robot, logger));
		buffer.put(new LeftMovement(0, 90, robot, logger));
		buffer.put(new ForwardMovement(20, robot, logger));
		buffer.put(new LeftMovement(0, 90, robot, logger));
		buffer.put(new ForwardMovement(20, robot, logger));
		buffer.put(new LeftMovement(0, 90, robot, logger));
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