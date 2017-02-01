package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import common.UserManagementInterface;
import model.User;

@Stateless
public class RoleBean implements UserManagementInterface {
	@PersistenceContext(unitName = "userManagement-JPA")
	private EntityManager entityManager;
	public int add(String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int remove(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int update(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return null;
	}
}
