package common;

import java.util.List;

import exception.EntityOperationException;
import model.Role;
import model.User;

public interface UserManagementInterface extends UserAndRolesManagementInterface {
	public int update(User user)throws EntityOperationException;
	public int addRole(Role role) throws EntityOperationException;
	public List<User> getAllUser() throws EntityOperationException;
	public User getById(int id) throws EntityOperationException;
}
