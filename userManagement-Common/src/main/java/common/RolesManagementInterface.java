package common;

import java.util.List;

import exception.EntityOperationException;
import exception.ManagedBeanException;
import model.Role;
import model.User;

public interface RolesManagementInterface extends UserAndRolesManagementInterface {
	public List<Role> getAllRoles() throws EntityOperationException, ManagedBeanException;

	public int update(Role role) throws EntityOperationException, ManagedBeanException;

	public Role searchRole(String message) throws ManagedBeanException;

	public Role getById(int id) throws EntityOperationException, ManagedBeanException;
}
