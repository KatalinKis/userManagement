package common;

import java.util.List;

import exception.EntityOperationException;
import model.IEntity;

public interface UserAndRolesManagementInterface {	
	public int add (String username) throws EntityOperationException;
	public int remove(int id) throws EntityOperationException;
}
