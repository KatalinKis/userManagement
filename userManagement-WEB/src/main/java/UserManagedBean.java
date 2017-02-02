import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.UserManagementInterface;
import model.IEntity;
import model.User;

@Named("userBean")
@ApplicationScoped
public class UserManagedBean implements Serializable, UserManagementInterface {

	private static final long serialVersionUID = -3133632987180492075L;
	private UserManagementInterface userManagement;
	private String searchId;
	private String username;

	private UserManagementInterface getUserManagement() {
		if (userManagement == null) {
			try {
				InitialContext jndi = new InitialContext();
				userManagement = (UserManagementInterface) jndi.lookup(
						"java:global/userManagement-EAR-0.0.1-SNAPSHOT/userManagement-EJB-0.0.1-SNAPSHOT/UserBean");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userManagement;
	}

	public List<?> getAll() {
		return getUserManagement().getAllUser();
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

	public int addUser() {
		return add(username);
	}

	public int add(String username) {
		return getUserManagement().add(username);
	}

	public int remove(int id) {
		return getUserManagement().remove(id);
	}

	public int update(User user) {
		return getUserManagement().update(user);
	}

	public List<User> getAllUser() {
		return getUserManagement().getAllUser();
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

}
