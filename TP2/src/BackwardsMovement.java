public class BackwardsMovement extends Movement {

	private int distance;
	private RobotLegoEV3 robot;

	public BackwardsMovement(int distance, RobotLegoEV3 robot, ILogger logger) {
		super(logger);
		this.distance = distance;
		this.robot = robot;
	}

	public void doMovement() {
		robot.Reta(-this.distance);
		log("O robô andou para trás " + Math.abs(this.distance) + " centímetros.\n");
	}

	public int getTime() {
		return (int) ((distance / 0.02) + 100);
	}
}
