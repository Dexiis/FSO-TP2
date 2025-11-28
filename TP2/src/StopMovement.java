public class StopMovement extends Movement {
	private RobotLegoEV3 robot;

	public StopMovement(RobotLegoEV3 robot, ILogger logger) {
		super(logger);
		this.robot = robot;
	}

	public void doMovement() {
		robot.Parar(true);
		log("O robô parou. ");
	}

	public int getTime() {
		return 100;
	}

}
