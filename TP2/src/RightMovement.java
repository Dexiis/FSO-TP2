public class RightMovement extends Movement {

	private int radius, angle;
	private RobotLegoEV3 robot;

	public RightMovement(int radius, int angle, RobotLegoEV3 robot, ILogger logger) {
		super(logger);
		this.radius = radius;
		this.angle = angle;
		this.robot = robot;
	}

	public void doMovement() {
		robot.CurvarDireita(this.radius, this.angle);
		log("O robô curvou à direita com um ângulo de " + this.angle + " graus e com um raio de " + this.radius
				+ " centímetros.\n");
	}

	public int getTime() {
		return (int) (((Math.toRadians(this.angle) * this.radius) / 0.02) + 100);
	}

}
