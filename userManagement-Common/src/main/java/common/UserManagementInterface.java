package common;

import java.util.List;

import exception.ManagedBeanException;
import exception.EntityOperationException;
import model.Role;
import model.User;

public interface UserManagementInterface extends UserAndRolesManagementInterface {
	public int update(User user)throws EntityOperationException, ManagedBeanException;
	public int addRole(Role role) throws EntityOperationException, ManagedBeanException;
	public int addRoleUpdate(Role requestedRole, User userToUpdate) throws EntityOperationException, ManagedBeanException;
	public List<User> getAllUser() throws EntityOperationException, ManagedBeanException;
	public User getById(int id) throws EntityOperationException, ManagedBeanException;
}
