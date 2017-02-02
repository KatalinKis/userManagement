package common;

import java.util.List;

import model.Role;
import model.User;

public interface RolesManagementInterface extends UserAndRolesManagementInterface {
	public List<Role> getAllRoles();

	public int update(Role role);

	public Role searchRole(String message);

	public Role getById(int id);
}
