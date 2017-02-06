import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import common.RolesManagementInterface;
import exception.EntityOperationException;
import exception.ManagedBeanException;
import model.Role;
import model.User;

@Named("roleBean")
@ApplicationScoped
public class RoleManagedBean implements Serializable, RolesManagementInterface {
	private RolesManagementInterface roleManagement;
	private String searchId;
	private String rolename;
	private boolean exception;
	private final String internalError = "Internal error!";
	List<Role> roles = new ArrayList<Role>();
	private Logger oLogger = Logger.getLogger(UserManagedBean.class);

	private RolesManagementInterface getRoleManagement() {
		if (roleManagement == null) {
			try {
				InitialContext jndi = new InitialContext();
				roleManagement = (RolesManagementInterface) jndi.lookup(
						"java:global/userManagement-EAR-0.0.1-SNAPSHOT/userManagement-EJB-0.0.1-SNAPSHOT/RoleBean");
			} catch (NamingException e) {
				ErrorManagedBean.getErrorBean().setErrorMessage(internalError);
				oLogger.error(e);
			}
		}
		return roleManagement;
	}

	private boolean checkInputField(String inputField) throws ManagedBeanException {
		boolean ok = true;
		if (inputField.length() == 0) {
			ok = false;
			throw new ManagedBeanException("Input field can't be empty!");
		}
		return ok;
	}

	public int add(String rolename) throws ManagedBeanException {
		try {
			getRoleManagement().add(rolename);
		} catch (EntityOperationException e) {
			throw new ManagedBeanException("Couldn't add role. Possibly the role already exists.", e);
		}
		return 0;
	}

	public int addRole() throws ManagedBeanException {
		try {
			if (checkInputField(rolename)) {
				add(rolename);
			}
		} catch (ManagedBeanException e) {
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException("Internal error!", e);
		}
		clearInputFields();
		return 0;
	}

	public int remove(int id) throws ManagedBeanException {
		try {
			getRoleManagement().remove(id);
		} catch (EntityOperationException e) {
			throw new ManagedBeanException(
					"Couldn't remove role. Possibly the specified role does not exist or is bound to one or more user.",
					e);
		} catch (ManagedBeanException e) {
			throw new ManagedBeanException(
					"Couldn't remove role. Possibly the specified role does not exist or is bound to one or more user.");
		} catch (Exception e) {
			throw new ManagedBeanException(
					"Couldn't remove role. Possibly the specified role does not exist or is bound to one or more user.");
		}
		return 0;
	}

	public int removeRole() throws ManagedBeanException {
		try {
			if (checkInputField(searchId)) {
				remove(Integer.parseInt(searchId));
			}
		} catch (NumberFormatException e) {
			oLogger.error(e);
			ErrorManagedBean.getErrorBean().setErrorMessage("User id must be a number!");
			throw new ManagedBeanException(internalError);
		} catch (ManagedBeanException e) {
			oLogger.error(e);
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		}
		clearInputFields();
		return 0;
	}

	public int update(Role role) throws ManagedBeanException {
		try {
			getRoleManagement().update(role);
		} catch (EntityOperationException e) {
			throw new ManagedBeanException("Couldn't update user!", e);
		}
		return 0;
	}

	public int updateRole() throws ManagedBeanException {
		Role role = new Role();
		try {
			if (checkInputField(searchId) && checkInputField(rolename)) {
				role.setId(Integer.parseInt(searchId));
				role.setRole(rolename);
				update(role);
			}
		} catch (NumberFormatException e) {
			oLogger.error(e);
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		} catch (ManagedBeanException e) {
			oLogger.error(e);
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		}
		clearInputFields();
		return 0;
	}

	public List<Role> getAllRoles() throws ManagedBeanException {
		try {
			roles = getRoleManagement().getAllRoles();
		} catch (EntityOperationException e) {
			throw new ManagedBeanException("There are no users!", e);
		}
		return roles;
	}

	public Role searchRole(String message) throws ManagedBeanException {
		return getRoleManagement().searchRole(message);
	}

	public Role searchRoleByName() throws ManagedBeanException {
		return searchRole(rolename);
	}

	public List<Role> getAll() throws ManagedBeanException {
		try {
			roles = getAllRoles();
		} catch (ManagedBeanException e) {
			oLogger.error(e);
			ErrorManagedBean.getErrorBean().setErrorMessage(e.getMessage());
			throw new ManagedBeanException(internalError);
		}
		clearInputFields();
		return roles;
	}

	public Role getById(int id) throws ManagedBeanException {
		Role role = new Role();
		try {
			role = getRoleManagement().getById(id);
		} catch (EntityOperationException e) {
			throw new ManagedBeanException("No user with given id!", e);
		}
		return role;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public boolean isException() {
		return exception;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}

	private void clearInputFields() {
		searchId = "";
		rolename = "";
	}

}
