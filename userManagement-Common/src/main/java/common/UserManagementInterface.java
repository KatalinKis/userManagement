package common;

import java.util.List;

import model.User;

public interface UserManagementInterface extends UserAndRolesManagementInterface{
	public int update(User user);
	public List<User> getAllUser();
}
