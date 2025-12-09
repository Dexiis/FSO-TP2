public class RightMovement extends Movement {

	private int radius, angle;
	private RobotLegoEV3 robot;

	public RightMovement(int radius, int angle, RobotLegoEV3 robot, ILogger logger) {
		super(logger);
		this.radius = radius;
		this.angle = angle;
		this.robot = robot;
		this.id = 'r';
	}

	public void doMovement() {
		robot.CurvarDireita(this.radius, this.angle);
		log("O robô curvou à direita com um ângulo de " + this.angle + " graus e com um raio de " + this.radius
				+ " centímetros.\n");
	}

	public int getTime() {
		return (int) (((Math.toRadians(this.angle) * this.radius) / 0.02) + 100);
	}
	
	@Override
	public byte[] serializedMovement() {
		byte[] movement = new byte[FIXED_LENGTH];
		movement[0] = this.id;
		movement[1] = (byte) (radius >> 24);
		movement[2] = (byte) (radius >> 16);
		movement[3] = (byte) (radius >> 8);
		movement[4] = (byte) radius;
		movement[5] = (byte) (angle >> 24);
		movement[6] = (byte) (angle >> 16);
		movement[7] = (byte) (angle >> 8);
		movement[8] = (byte) angle;
		return movement;
	}
	
}
