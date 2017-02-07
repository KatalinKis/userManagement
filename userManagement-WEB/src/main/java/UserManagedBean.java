import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import common.UserManagementInterface;
import exception.EntityOperationException;
import exception.ManagedBeanException;
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
	private final String INTERNAL_ERROR = "Internal error!";
	private Logger oLogger = Logger.getLogger(UserManagedBean.class);

	private UserManagementInterface getUserManagement() {
		if (userManagement == null) {
			try {
				InitialContext jndi = new InitialContext();
				userManagement = (UserManagementInterface) jndi.lookup(
						"java:global/userManagement-EAR-0.0.1-SNAPSHOT/userManagement-EJB-0.0.1-SNAPSHOT/UserBean");
			} catch (NamingException e) {
				ErrorManagedBean.getErrorBean().setErrorMessage(Commons.getInstance().INTERNAL_ERROR);
				oLogger.error(e);
			}
		}
		return userManagement;
	}

	public List<?> getAll() throws ManagedBeanException {
		List<User> users = new ArrayList<User>();
		try {
			users = getUserManagement().getAllUser();
		} catch (EntityOperationException e) {
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
		} catch (ManagedBeanException e) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_DATA);
		}
		clearInputFields();
		return users;
	}

	public int removeUser() throws ManagedBeanException {
		try {
			if (Commons.getInstance().checkInputField(searchId)) {
				remove(Integer.parseInt(searchId));
			} else {
				Commons.getInstance().throwEmptyFieldError();
			}
		} catch (NumberFormatException e) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_DATA);
		} catch (ManagedBeanException e) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_DATA);
		}
		clearInputFields();
		return 0;
	}

	public int updateUser() throws ManagedBeanException {
		User userToUpdate = new User();
		boolean isUserNameProvided = false;
		boolean isRoleProvided = false;
		boolean isIdprovided = false;
		try {
			if (Commons.getInstance().checkInputField(searchId)) {
				isIdprovided = true;
				userToUpdate = getUserManagement().getById(Integer.parseInt(searchId));
			}
			if (Commons.getInstance().checkInputField(username)) {
				isUserNameProvided = true;
				userToUpdate.setUsername(username);
			} else {
				userToUpdate.setUsername(userToUpdate.getUsername());
			}
			try {
				if (Commons.getInstance().checkInputField(role)) {
					if (!userToUpdate.getRoles().contains(role)) {
						isRoleProvided = true;
						Role requestedRole = getRole(role);
						addRoleUpdate(requestedRole, userToUpdate);
					}
				} else {
					userToUpdate.setRoles(userToUpdate.getRoles());
				}
				if ((isUserNameProvided && !isRoleProvided && isIdprovided)
						|| (!isUserNameProvided && isRoleProvided && isIdprovided)
						|| (isUserNameProvided && isRoleProvided && isIdprovided)) {
					update(userToUpdate);
				} else {
					Commons.getInstance().throwException(Commons.getInstance().INVALID_DATA);
				}
			} catch (ManagedBeanException e) {
				Commons.getInstance().throwException(Commons.getInstance().EMPTY_FIELD);
			} catch (Exception e) {
				Commons.getInstance().throwException(Commons.getInstance().EMPTY_FIELD);
			}
		} catch (ManagedBeanException e) {
			Commons.getInstance().throwException(Commons.getInstance().EMPTY_FIELD);
		} catch (NumberFormatException e) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_ID);
		} catch (EntityOperationException e) {
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
		}
		clearInputFields();
		return 0;
	}

	public int addRoleUpdate(Role requestedRole, User userToUpdate) throws ManagedBeanException {
		try {
			getUserManagement().addRoleUpdate(requestedRole, userToUpdate);
		} catch (EntityOperationException e) {
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
		} catch (ManagedBeanException e) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_DATA);
		}
		return 0;
	}

	public int addRole(Role role) throws ManagedBeanException {
		try {
			getUserManagement().addRole(role);
		} catch (EntityOperationException e) {
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
		}
		return 0;
	}

	public int addUser() throws ManagedBeanException {
		try {
			if (Commons.getInstance().checkInputField(username) && Commons.getInstance().checkInputField(role)) {
				add(username);
			} else {
				Commons.getInstance().throwEmptyFieldError();
			}
		} catch (ManagedBeanException e) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_DATA);
		}
		clearInputFields();
		return 0;
	}

	public int add(String username) throws ManagedBeanException {
		Role requestedRole = getRole(role);
		addRole(requestedRole);
		try {
			getUserManagement().add(username);
		} catch (EntityOperationException e) {
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
		}
		return 0;
	}

	public int remove(int id) throws ManagedBeanException {
		try {
			getUserManagement().remove(id);
		} catch (EntityOperationException e) {
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
		}
		return 0;
	}

	public int update(User user) throws ManagedBeanException {
		boolean updated = false;
		for (int i = 0; i < getAllUser().size(); ++i) {
			oLogger.info(user.getId() == getAllUser().get(i).getId());
			if (user.getId() == getAllUser().get(i).getId()) {
				try {
					getUserManagement().update(user);
					updated = true;
				} catch (EntityOperationException e) {
					oLogger.error("Couldn't update user!");
				}
			}
		}
		if (!updated) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_DATA);
		}
		return 0;
	}

	public List<User> getAllUser() throws ManagedBeanException {
		List<User> users = new ArrayList<User>();
		try {
			users = getUserManagement().getAllUser();
		} catch (EntityOperationException e) {
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
		}
		return users;
	}

	public User getById(int id) throws ManagedBeanException {
		User user = new User();
		try {
			user = getUserManagement().getById(id);
		} catch (EntityOperationException e) {
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
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

	public boolean isException() {
		return exception;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}

	private Role getRole(String role) throws ManagedBeanException {
		RoleManagedBean rmb = new RoleManagedBean();
		List<Role> roles = rmb.getAllRoles();
		oLogger.info(roles);
		Role requestedRole = null;
		boolean found = false;
		for (int i = 0; i < roles.size(); ++i) {
			if (roles.get(i).getRole().equals(role)) {
				found = true;
				requestedRole = roles.get(i);
			}
		}
		if (!found) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_DATA);
		}
		return requestedRole;
	}

	private void clearInputFields() {
		searchId = "";
		username = "";
		role = "";
	}
}
