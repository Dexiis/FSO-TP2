public class Data {

	private int distance;
	private int angle;
	private int radius;
	private int actionNumber;
	private String name;

	public Data(int radius, int angle, int distance, int actionNumber, String name) {
		this.distance = distance;
		this.angle = angle;
		this.radius = radius;
		this.actionNumber = actionNumber;
		this.name = name;
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

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getActionNumber() {
		return actionNumber;
	}

	public void setActionNumber(int actionNumber) {
		this.actionNumber = actionNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDelayStraightLine() {
		return (int) ((distance / 0.02) + 100);
	}

	public int getDelayCurve() {
		return (int) (((Math.toRadians(this.angle) * this.radius) / 0.02) + 100);
	}
}