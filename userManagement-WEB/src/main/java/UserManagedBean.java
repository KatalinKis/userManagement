import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.text.html.parser.Entity;

import common.UserManagementInterface;
import exception.EntityOperationException;
import exception.ManagedBeanException;
import model.IEntity;
import model.Role;
import model.User;
import org.jboss.logging.Logger;

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
	private final String internalError = "Internal error!";
	private Logger oLogger = Logger.getLogger(UserManagedBean.class);

	private UserManagementInterface getUserManagement() {
		if (userManagement == null) {
			try {
				InitialContext jndi = new InitialContext();
				userManagement = (UserManagementInterface) jndi.lookup(
						"java:global/userManagement-EAR-0.0.1-SNAPSHOT/userManagement-EJB-0.0.1-SNAPSHOT/UserBean");
			} catch (NamingException e) {
				ErrorManagedBean.getErrorBean().getErrorBean().setErrorMessage(internalError);
				oLogger.error(e);
			}
		}
		return userManagement;
	}

	private boolean checkInputField(String inputField) throws ManagedBeanException {
		boolean ok = true;
		if (inputField.length() == 0) {
			ok = false;
			throw new ManagedBeanException("Input field can't be empty!");
		}
		return ok;
	}

	public List<?> getAll() throws ManagedBeanException {
		List<User> users = new ArrayList<User>();
		try {
			users = getUserManagement().getAllUser();
		} catch (EntityOperationException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
		} catch (ManagedBeanException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		}
		return users;
	}

	public int removeUser() throws ManagedBeanException {
		try {
			if (checkInputField(searchId)) {
				remove(Integer.parseInt(searchId));
			}
		} catch (NumberFormatException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage("User id must be a number!");
			e.printStackTrace();
		} catch (ManagedBeanException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		}
		return 0;
	}

	public int updateUser() throws ManagedBeanException {
		User user = new User();
		try {
			if (checkInputField(searchId) && checkInputField(username)) {
				user.setId(Integer.parseInt(searchId));
				user.setUsername(username);
				try {
					update(user);
				} catch (ManagedBeanException e) {
					ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
					throw new ManagedBeanException(internalError);
				} catch (Exception e) {
					ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
					throw new ManagedBeanException(internalError);
				}
			}
		} catch (ManagedBeanException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		} catch (NumberFormatException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage("Id must be a number!");
			throw new ManagedBeanException(internalError);
		}
		return 0;
	}

	public int addRole(Role role) throws ManagedBeanException {
		try {
			getUserManagement().addRole(role);
		} catch (EntityOperationException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		}
		return 0;
	}

	public int addUser() throws ManagedBeanException {
		try {
			if (checkInputField(username) && checkInputField(role)) {
				add(username);
			}
		} catch (ManagedBeanException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		}
		return 0;
	}

	public int add(String username) throws ManagedBeanException {
		RoleManagedBean rmb = new RoleManagedBean();
		List<Role> roles = rmb.getAllRoles();
		Role requestedRole = null;
		for (int i = 0; i < roles.size(); ++i) {
			if (roles.get(i).getRole().equals(role)) {
				requestedRole = roles.get(i);
			} else {
				throw new ManagedBeanException("The specified role does not exist!");
			}
		}
		addRole(requestedRole);
		try {
			getUserManagement().add(username);
		} catch (EntityOperationException e) {
			throw new ManagedBeanException("Unable to add user!", e);
		}
		return 0;
	}

	public int remove(int id) throws ManagedBeanException {
		try {
			getUserManagement().remove(id);
		} catch (EntityOperationException e) {
			throw new ManagedBeanException(internalError, e);
		}
		return 0;
	}

	public int update(User user) throws ManagedBeanException {
		for (int i = 0; i < getAllUser().size(); ++i) {
			if (user.getId() == getAllUser().get(i).getId()) {
				try {
					getUserManagement().update(user);
				} catch (EntityOperationException e) {
					throw new ManagedBeanException("Couldn't update user!", e);
				}
			} else {
				throw new ManagedBeanException("The specified user doesn't exist!");
			}
		}
		return 0;
	}

	public List<User> getAllUser() throws ManagedBeanException {
		List<User> users = new ArrayList<User>();
		try {
			users = getUserManagement().getAllUser();
		} catch (EntityOperationException e) {
			throw new ManagedBeanException("There are no users!", e);
		}
		return users;
	}

	public User getById(int id) throws ManagedBeanException {
		User user = new User();
		try {
			user = getUserManagement().getById(id);
		} catch (EntityOperationException e) {
			throw new ManagedBeanException("No user with given id!", e);
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

}
