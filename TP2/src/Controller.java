public class Controller implements Runnable {

	private final Data data = new Data();
	private final RobotLegoEV3 robot = new RobotLegoEV3();
	private final RandomMovements randomMovements;
	private final AvoidObstacle avoidObstacle;
	private final AccessManager robotManager = new AccessManager();
	private final AccessManager bufferManager = new AccessManager();
	private final Buffer buffer = new Buffer();
	private final Thread randomMovementsThread;
	private final Thread avoidObstacleThread;
	private final Record record;

	public boolean robotOn = false;

	private ILogger logger;
	private Movement movement;
	private StateEnum bufferState = StateEnum.IDLE;

	private long waitingTime;

	public Controller(ILogger logger, Record record) {
		this.logger = logger;
		this.record = record;
		
		this.randomMovements = new RandomMovements(robot, logger, this, bufferManager);
		this.randomMovementsThread = new Thread(randomMovements);
		this.randomMovementsThread.start();
		
		this.avoidObstacle = new AvoidObstacle(robot, logger, this, bufferManager, robotManager);
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
				} else
					bufferState = StateEnum.EXECUTE;
				break;
			default:
				break;
			}
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
		robotManager.acquire();
		robot.OpenEV3(name);
		robotManager.release();
		this.robotOn = true;
	}

	public void turnOffRobot() {
		robotManager.acquire();
		robot.CloseEV3();
		robotManager.release();
		this.robotOn = false;
	}

	public synchronized void bufferMoveForward() {
		Movement m = new ForwardMovement(data.getDistance(), robot, logger);
		bufferManager.acquire();
		buffer.put(m);
		recordMovement(m);
		bufferManager.release();
		notify();
	}

	public synchronized void bufferMoveBackwards() {
		Movement m = new BackwardsMovement(data.getDistance(), robot, logger);
		bufferManager.acquire();
		buffer.put(m);
		recordMovement(m);
		bufferManager.release();
		notify();
	}

	public synchronized void bufferMoveRightCurve() {
		Movement m = new RightMovement(data.getRadius(), data.getAngle(), robot, logger);
		bufferManager.acquire();
		buffer.put(m);
		recordMovement(m);
		bufferManager.release();
		notify();
	}

	public synchronized void bufferMoveLeftCurve() {
		Movement m = new LeftMovement(data.getRadius(), data.getAngle(), robot, logger);
		bufferManager.acquire();
		buffer.put(m);
		recordMovement(m);
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
	
	public synchronized void bufferManagerAcquire() {
		bufferManager.acquire();
	}
	
	public synchronized void bufferManagerRelease() {
		bufferManager.release();
	}

	public void stopMovement() {
		Movement m = new StopMovement(robot, logger);
		robotManager.acquire();
		m.doMovement();
		recordMovement(m);
		robotManager.release();
		this.waitingTime = 0;
	}

	public void stopMovementSync() {
		robotManager.acquire();
		robot.Parar(false);
		robotManager.release();
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
	
	public RobotLegoEV3 getRobot() {
		return this.robot;
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
	
	private void recordMovement(Movement movementToRecord) {
		if (record.isRecording() && (GUI) this.logger instanceof RandomMovementsGUI) {
			record.recordMovement(movementToRecord);
		}
	}
	
}