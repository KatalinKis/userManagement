package exception;

public class EntityOperationException extends Exception {
	private static final long serialVersionUID = 7650786580091098036L;

	public EntityOperationException() {
		super();
	}

	public EntityOperationException(String message) {
		super(message);
	}

	public EntityOperationException(String message, Exception e) {
		super(message, e);
	}
}
