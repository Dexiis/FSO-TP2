public class StopMovement extends Movement {
	private RobotLegoEV3 robot;

	public StopMovement(RobotLegoEV3 robot, ILogger logger) {
		super(logger);
		this.robot = robot;
		this.id = 's';
	}

	public void doMovement() {
		robot.Parar(true);
		log("O robô parou. \n");
	}

	public int getTime() {
		return 100;
	}

	@Override
	public byte[] serializedMovement() {
		byte[] movement = new byte[FIXED_LENGTH];
		movement[0] = id;
		return movement;
	}
}