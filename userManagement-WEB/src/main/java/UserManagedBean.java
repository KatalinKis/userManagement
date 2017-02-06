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
			if (checkInputField(searchId)) {
				user.setId(Integer.parseInt(searchId));
			}
			if (checkInputField(username)) {
				user.setUsername(username);
			}
			if (checkInputField(role)) {
				try {
					oLogger.info("/////********----- checkInput role ok," + user.getId());
					User userToUpdate = getUserManagement().getById(user.getId());
					oLogger.info("userToUpdate: " + userToUpdate.getId());

					oLogger.info((userToUpdate.getRoles().contains(role) ? "van ilzen role" : " nincs ilzen role"));

					if (userToUpdate.getRoles().contains(role)) {
						oLogger.info("got role" + role);
						ErrorManagedBean.getErrorBean().setErrorMessage("User already has specified role.");
						throw new ManagedBeanException("User already has specified role.");
					} else {
						Role requestedRole = getRole(role);
						oLogger.info("got req role " + requestedRole.getRole());
						addRole(requestedRole);
						oLogger.info("after added got req role " + requestedRole.getRole());
						update(user);
					}
				} catch (ManagedBeanException e) {
					oLogger.info("catch managed exception");
					ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
					throw new ManagedBeanException(internalError);
				} catch (Exception e) {
					oLogger.info("catch exception");
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
			oLogger.info(role.getRole());
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
		Role requestedRole = getRole(role);
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
		boolean updated = false;
		for (int i = 0; i < getAllUser().size(); ++i) {
			oLogger.info(user.getId() == getAllUser().get(i).getId());
			if (user.getId() == getAllUser().get(i).getId()) {
				try {
					oLogger.info("inside update" + user.getUsername());
					getUserManagement().update(user);
					oLogger.info("after update user");
					updated = true;
				} catch (EntityOperationException e) {
					oLogger.error("Couldn't update user!");
				}
			
			}			
		}
		 if(!updated){
				throw new ManagedBeanException("Specified user doesn't exist!");
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

	private Role getRole(String role) throws ManagedBeanException {
		RoleManagedBean rmb = new RoleManagedBean();
		List<Role> roles = rmb.getAllRoles();
		oLogger.info(roles);
		Role requestedRole = null;
		boolean found = false;
		for (int i = 0; i < roles.size(); ++i) {
			oLogger.info("-*********" + roles.get(i).getRole() + " " + role);
			if (roles.get(i).getRole().equals(role)) {
				found = true;
				requestedRole = roles.get(i);
				oLogger.info(requestedRole);
			}
		}
		if (!found) {
			throw new ManagedBeanException("The specified role does not exist!");
		}
		return requestedRole;
	}
}
