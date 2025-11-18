public class RobotController implements Runnable {

	private final Data data = new Data(0, 0, 0, 0, null);
	private final RobotLegoEV3 robot = new RobotLegoEV3();
	private final RandomMovements randomMovements;
	private final AvoidObstacle avoidObstacle;
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
		this.randomMovements = new RandomMovements(robot, logger, this, bufferManager);
		this.avoidObstacle = new AvoidObstacle(robot, logger, this, bufferManager);
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
				if (!buffer.isEmpty())
					bufferState = StateEnum.EXECUTE;
				break;
			case EXECUTE:
				movement = buffer.get();
				movement.doMovement();
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
		buffer.put(new Movement(robot, logger, MovementEnum.FORWARD, data.getDistance()));
		notify();
	}

	public synchronized void bufferMoveBackwards() {
		buffer.put(new Movement(robot, logger, MovementEnum.BACKWARDS, data.getDistance()));
		notify();
	}

	public synchronized void bufferMoveRightCurve() {
		buffer.put(new Movement(robot, logger, MovementEnum.RIGHT, data.getRadius(), data.getAngle()));
		notify();
	}

	public synchronized void bufferMoveLeftCurve() {
		buffer.put(new Movement(robot, logger, MovementEnum.LEFT, data.getRadius(), data.getAngle()));
		notify();
	}

	public synchronized void bufferStopMovement() {
		buffer.put(new Movement(robot, logger, MovementEnum.STOP));
		notify();
	}
	
	public synchronized boolean obstacleFound() {
		/* MODO ROBO */
		return robot.SensorToque(robot.S_1) == 1;
		
		/* MODO TESTE
		return sensorTest;
		*/
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
}