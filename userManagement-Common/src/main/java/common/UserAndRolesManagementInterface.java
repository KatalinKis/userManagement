package common;

import java.util.List;

import exception.EntityOperationException;
import exception.ManagedBeanException;
import model.IEntity;

public interface UserAndRolesManagementInterface {	
	public int add (String username) throws EntityOperationException, ManagedBeanException;
	public int remove(int id) throws EntityOperationException, ManagedBeanException;
}
