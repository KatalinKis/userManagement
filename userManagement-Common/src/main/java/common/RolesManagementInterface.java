package common;

import java.util.List;

import model.Role;

public interface RolesManagementInterface extends UserAndRolesManagementInterface{
	public List<Role> getAllRoles();
	public int update(Role role);
}
