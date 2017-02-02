package common;

import java.util.List;

import model.Role;
import model.User;

public interface UserManagementInterface extends UserAndRolesManagementInterface {
	public int update(User user);
	public int addRole(Role role);
	public List<User> getAllUser();
	public User getById(int id);
}
