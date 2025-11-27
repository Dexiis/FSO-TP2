public abstract class Movement {
	private ILogger logger;

	public Movement(ILogger logger) {
		this.logger = logger;
	}

	public abstract void doMovement();

	public abstract int getTime();

	protected void log(String message) {
		if (logger != null) {
			logger.logMessage(message);
		}
	}
}