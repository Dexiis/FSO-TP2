public class ForwardMovement extends Movement {

	private int distance;
	private RobotLegoEV3 robot;

	public ForwardMovement(int distance, RobotLegoEV3 robot, ILogger logger) {
		super(logger);
		this.distance = distance;
		this.robot = robot;
		this.id = 'f';
	}

	public void doMovement() {
		robot.Reta(this.distance);
		log("O robô andou para a frente " + Math.abs(this.distance) + " centímetros.\n");
	}

	public int getTime() {
		return (int) ((distance / 0.02) + 100);
	}

	@Override
	public byte[] serializedMovement() {
		byte[] movement = new byte[FIXED_LENGTH];
		movement[0] = this.id;
		movement[1] = (byte) (distance >> 24);
		movement[2] = (byte) (distance >> 16);
		movement[3] = (byte) (distance >> 8);
		movement[4] = (byte) distance;
		return movement;
	}
}
