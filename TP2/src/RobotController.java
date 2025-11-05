public class RobotController implements Runnable {

	private final Data data = new Data(0, 0, 0, 0, null);
	private final RobotLegoEV3 robot = new RobotLegoEV3();
	private final RandomMovements randomMovements = new RandomMovements(this);
	private final Buffer buffer = new Buffer();
	private final Thread randomMovementsThread;

	private ILogger logger;

	private Movement movement;
	private StateEnum bufferState = StateEnum.IDLE;

	private long waitingTime;
	private long timeStamp;

	public RobotController() {
		this.randomMovementsThread = new Thread(randomMovements);
		this.randomMovementsThread.start();
	}

	public void run() {
		while (true) {
			switch (bufferState) {
			case IDLE:
				if (!buffer.isEmpty())
					bufferState = StateEnum.EXECUTE;
				break;
			case EXECUTE:
				movement = buffer.get();
				movement.doMovement();
				waitingTime = movement.getTime();
				timeStamp = System.currentTimeMillis();
				bufferState = StateEnum.WAIT;
				break;
			case WAIT:
				if (System.currentTimeMillis() - timeStamp >= waitingTime)
					if (buffer.isEmpty()) {
						bufferState = StateEnum.IDLE;
						stopMovementSync();
					} else {
						bufferState = StateEnum.EXECUTE;
					}
				break;
			}
		}
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
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

	public void updateData(int radius, int angle, int distance) {
		data.setRadius(radius);
		data.setAngle(angle);
		data.setDistance(distance);
	}

	public void turnOnRobot() {
		robot.OpenEV3(data.getName());
	}

	public void turnOffRobot() {
		// robot.CloseEV3();
	}

	public void moveForward() {
		buffer.put(new Movement(robot, logger, MovementEnum.FORWARD, data.getDistance()));
	}

	public void moveBackwards() {
		buffer.put(new Movement(robot, logger, MovementEnum.BACKWARDS, data.getDistance()));
	}

	public void moveRightCurve() {
		buffer.put(new Movement(robot, logger, MovementEnum.RIGHT, data.getRadius(), data.getAngle()));
	}

	public void moveLeftCurve() {
		buffer.put(new Movement(robot, logger, MovementEnum.LEFT, data.getRadius(), data.getAngle()));
	}

	public void bufferStopMovement() {
		buffer.put(new Movement(robot, logger, MovementEnum.STOP));
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
		buffer.put(new Movement(robot, logger, MovementEnum.FORWARD, 20));
		buffer.put(new Movement(robot, logger, MovementEnum.LEFT, 0, 90));
		buffer.put(new Movement(robot, logger, MovementEnum.FORWARD, 20));
		buffer.put(new Movement(robot, logger, MovementEnum.LEFT, 0, 90));
		buffer.put(new Movement(robot, logger, MovementEnum.FORWARD, 20));
		buffer.put(new Movement(robot, logger, MovementEnum.LEFT, 0, 90));
		buffer.put(new Movement(robot, logger, MovementEnum.FORWARD, 20));
		buffer.put(new Movement(robot, logger, MovementEnum.LEFT, 0, 90));
		stopMovementSync();
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

	public int getDelayStraightLine() {
		return data.getDelayStraightLine();
	}

	public int getDelayCurve() {
		return data.getDelayCurve();
	}
}