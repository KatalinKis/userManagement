package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import common.UserManagementInterface;
import model.IEntity;
import model.Role;
import model.User;


@Stateless
public class UserBean implements UserManagementInterface {

	@PersistenceContext(unitName ="userManagement-JPA") 
	private EntityManager entityManager;
	List<Role> roles = new ArrayList<Role>();
	

	@SuppressWarnings("unchecked")
	public List<User> getAllUser() {
		List<User> users = entityManager.createNamedQuery("User.findAll").getResultList();
		return users;		
	}

	public User getById(int id) {
		return entityManager.find(User.class, id);
	}

	public int add(String username) {
		int nr = ((Number) entityManager.createNamedQuery("User.countAll").getSingleResult()).intValue();
		User user = new User();
		user.setId(nr);
		user.setUsername(username);
		user.setRoles(roles);
		entityManager.persist(user);
		return 0;
	}

	public int remove(int id) {
		User user = getById(id);
		entityManager.remove(user);
		return 0;
	}

	public int update(User user) {
		entityManager.merge(user);
		return 0;
	}
	
	public int addRole(Role role){
		roles.add(role);
		return 0;
	}
}
