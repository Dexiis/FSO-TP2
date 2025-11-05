
public class Movement {

	private RobotLegoEV3 robot;

	private ILogger logger;
	private MovementEnum movement;
	private int distance, radius, angle;

	public Movement(RobotLegoEV3 robot, ILogger logger, MovementEnum movement, int distance) {
		this.logger = logger;
		this.movement = movement;
		this.distance = distance;
		this.robot = robot;
	}

	public Movement(RobotLegoEV3 robot, ILogger logger, MovementEnum movement, int radius, int angle) {
		this.logger = logger;
		this.movement = movement;
		this.radius = radius;
		this.angle = angle;
		this.robot = robot;
	}

	public Movement(RobotLegoEV3 robot, ILogger logger, MovementEnum movement) {
		this.logger = logger;
		this.movement = movement;
		this.robot = robot;
	}

	public MovementEnum getMovement() {
		return movement;
	}

	public void setMovement(MovementEnum movement) {
		this.movement = movement;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public void doMovement() {
		switch (this.movement) {
		case FORWARD:
			robot.Reta(this.distance);
			log("O robô andou para a frente " + this.distance + " centímetros.\n");
			break;
		case BACKWARDS:
			robot.Reta(-this.distance);
			log("O robô andou para trás " + Math.abs(this.distance) + " centímetros.\n");
			break;
		case RIGHT:
			robot.CurvarDireita(this.radius, this.angle);
			log("O robô curvou à direita com um ângulo de " + this.angle + " graus e com um raio de " + this.radius
					+ " centímetros.\n");
			break;
		case LEFT:
			robot.CurvarEsquerda(this.radius, this.angle);
			log("O robô curvou à esquerda com um ângulo de " + this.angle + " graus e com um raio de " + this.radius
					+ " centímetros.\n");
			break;
		case STOP:
			robot.Parar(true);
			log("O robô parou.\n");
			break;
		}
	}

	public int getTime() {
		if (this.movement == MovementEnum.FORWARD || this.movement == MovementEnum.BACKWARDS)
			return (int) ((distance / 0.02) + 100);
		else if (this.movement == MovementEnum.RIGHT || this.movement == MovementEnum.LEFT)
			return (int) (((Math.toRadians(this.angle) * this.radius) / 0.02) + 100);
		else
			return 100;

	}

	private void log(String message) {
		if (logger != null) {
			logger.logMessage(message);
		}
	}
}
