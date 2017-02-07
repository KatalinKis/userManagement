package util;
import org.jboss.logging.Logger;

import exception.EntityOperationException;

public class EjbCommons {
	private static EjbCommons instance;
	private Logger oLogger = Logger.getLogger(EjbCommons.class);
	public final String ENTITY_ERROR = "Entity exception caught.";

	private EjbCommons() {
	}

	public static EjbCommons getInstance() {
		if (instance == null) {
			instance = new EjbCommons();
		}
		return instance;
	}

	public void throwException(Exception e, String message) throws EntityOperationException {
		oLogger.error(e);
		throw new EntityOperationException(message, e);
	}
}
