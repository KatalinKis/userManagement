import org.jboss.logging.Logger;

import exception.ManagedBeanException;

public class Commons {
	private static Commons instance;
	private Logger oLogger = Logger.getLogger(Commons.class);
	public final String INTERNAL_ERROR = "Internal error!";
	public final String EMPTY_FIELD = "Field can't be empty!";
	public final String INVALID_DATA = "Please be sure that the input data is correct! Check that there is no entry with given name in case you are trying to add one or that it is an existing entry in case of deletion.";
	public final String INVALID_ID = "Id must be a number!";
	public final String INVALID_ROLE = "Couldn't remove role. Possibly the specified role does not exist or is bound to one or more user.";

	private Commons() {
	}

	public static Commons getInstance() {
		if (instance == null) {
			instance = new Commons();
		}
		return instance;
	}

	public boolean checkInputField(String inputField) {
		boolean ok = true;
		if (inputField.length() == 0) {
			ok = false;
		}
		return ok;
	}

	public void throwEmptyFieldError() throws ManagedBeanException {
		oLogger.error(EMPTY_FIELD);
		ErrorManagedBean.getErrorBean().setErrorMessage(EMPTY_FIELD);
		throw new ManagedBeanException(INTERNAL_ERROR);
	}

	public void throwException(String message) throws ManagedBeanException {
		oLogger.error(message);
		ErrorManagedBean.getErrorBean().setErrorMessage(message);
		throw new ManagedBeanException(message);
	}
}
