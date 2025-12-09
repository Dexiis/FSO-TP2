public abstract class Movement {
	private ILogger logger;

	protected static final int FIXED_LENGTH = 9;
	
	protected byte id;
	
	public Movement(ILogger logger) {
		this.logger = logger;
	}

	public abstract void doMovement();

	public abstract int getTime();

	public abstract byte[] serializedMovement();
	
	protected void log(String message) {
		if (logger != null) {
			logger.logMessage(message);
		}
	}
	
}