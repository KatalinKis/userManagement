package exception;

public class ManagedBeanException extends Exception {
	private static final long serialVersionUID = 7650512350091098036L;

	public ManagedBeanException() {
		super();
	}

	public ManagedBeanException(String message) {
		super(message);
	}

	public ManagedBeanException(String message, Exception e) {
		super(message, e);
	}

}
