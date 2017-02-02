import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.RolesManagementInterface;
import model.Role;
import model.User;

@Named("roleBean")
@ApplicationScoped
public class RoleManagedBean implements Serializable, RolesManagementInterface {
	private RolesManagementInterface roleManagement;
	private String searchId;
	private String rolename;

	private RolesManagementInterface getRoleManagement() {
		if (roleManagement == null) {
			try {
				InitialContext jndi = new InitialContext();
				roleManagement = (RolesManagementInterface) jndi.lookup(
						"java:global/userManagement-EAR-0.0.1-SNAPSHOT/userManagement-EJB-0.0.1-SNAPSHOT/RoleBean");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return roleManagement;
	}

	public int add(String rolename) {
		return getRoleManagement().add(rolename);
	}

	public int addRole() {
		return add(rolename);
	}

	public int remove(int id) {
		return getRoleManagement().remove(id);
	}

	public int removeRole() {
		return remove(Integer.parseInt(searchId));
	}

	public int update(Role role) {
		return getRoleManagement().update(role);
	}

	public int updateRole() {
		Role role = new Role();
		role.setId(Integer.parseInt(searchId));
		role.setRole(rolename);
		return update(role);
	}

	public List<Role> getAllRoles() {
		return getRoleManagement().getAllRoles();
	}

	public Role searchRole(String message) {
		return getRoleManagement().searchRole(message);
	}

	public Role searchRoleByName() {
		return searchRole(rolename);
	}

	public List<Role> getAll() {
		return getAllRoles();
	}

	public Role getById(int id) {
		return getRoleManagement().getById(id);
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

	@Override
	public int addUser(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

}
