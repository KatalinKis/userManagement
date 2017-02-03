import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.text.html.parser.Entity;

import common.UserManagementInterface;
import exception.EntityOperationException;
import model.IEntity;
import model.Role;
import model.User;

@Named("userBean")
@ApplicationScoped
public class UserManagedBean implements Serializable, UserManagementInterface {

	private static final long serialVersionUID = -3133632987180492075L;
	private UserManagementInterface userManagement;
	private String searchId;
	private String username;
	private String role;
	private String addSuccess;
	private boolean exception;

	private UserManagementInterface getUserManagement() {
		if (userManagement == null) {
			try {
				InitialContext jndi = new InitialContext();
				userManagement = (UserManagementInterface) jndi.lookup(
						"java:global/userManagement-EAR-0.0.1-SNAPSHOT/userManagement-EJB-0.0.1-SNAPSHOT/UserBean");
			} catch (NamingException e) {
				exception = true;
				// e.printStackTrace();
			}
		}
		return userManagement;
	}

	public List<?> getAll() {
		List<User> users = new ArrayList<>();
		try {
			users = getUserManagement().getAllUser();
		} catch (EntityOperationException e) {
			exception = true;
		}
		return users;
	}

	public int removeUser() {
		return remove(Integer.parseInt(searchId));
	}

	public int updateUser() {
		User user = new User();
		user.setId(Integer.parseInt(searchId));
		user.setUsername(username);
		return update(user);
	}

	public int addRole(Role role) {
		try {
			getUserManagement().addRole(role);
		} catch (EntityOperationException e) {
			exception = true;
		}
		return 0;
	}

	public int addUser() {
		return add(username);
	}

	public int add(String username) {
		RoleManagedBean rmb = new RoleManagedBean();
		List<Role> roles = rmb.getAllRoles();
		Role requestedRole = null;
		for (int i = 0; i < roles.size(); ++i) {
			if (roles.get(i).getRole().equals(role)) {
				requestedRole = roles.get(i);
				setAddSuccess("User added successfully!");
			} else {
				setAddSuccess("Couldn't add user!");
			}
		}
		addRole(requestedRole);
		try {
			getUserManagement().add(username);
		} catch (EntityOperationException e) {
			exception = true;
		}
		return 0;
	}

	public int remove(int id) {
		try {
			getUserManagement().remove(id);
		} catch (EntityOperationException e) {
			exception = true;
		}
		return 0;
	}

	public int update(User user) {
		try {
			getUserManagement().update(user);
		} catch (EntityOperationException e) {
			exception = true;
		}
		return 0;
	}

	public List<User> getAllUser() {
		List<User> users = new ArrayList<>();
		try {
			users = getUserManagement().getAllUser();
		} catch (EntityOperationException e) {
			exception = true;
		}
		return users;
	}

	public User getById(int id) {
		User user = new User();
		try {
			user = getUserManagement().getById(id);
		} catch (EntityOperationException e) {
			exception = true;
		}
		return user;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAddSuccess() {
		return addSuccess;
	}

	public void setAddSuccess(String addSuccess) {
		this.addSuccess = addSuccess;
	}
}
