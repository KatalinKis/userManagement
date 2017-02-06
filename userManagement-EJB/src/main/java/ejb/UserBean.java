package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import common.UserManagementInterface;
import exception.EntityOperationException;
import model.IEntity;
import model.Role;
import model.User;

@Stateless
public class UserBean implements UserManagementInterface {

	@PersistenceContext(unitName = "userManagement-JPA")
	private EntityManager entityManager;
	List<Role> roles = new ArrayList<Role>();
	private Logger oLogger = Logger.getLogger(RoleBean.class);

	@SuppressWarnings("unchecked")
	public List<User> getAllUser() throws EntityOperationException {
		List<User> users = new ArrayList<User>();
		try {
			users = entityManager.createNamedQuery("User.findAll").getResultList();
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return users;
	}

	public User getById(int id) throws EntityOperationException {
		User user = new User();
		try {
			user = entityManager.find(User.class, id);
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return user;
	}

	public int add(String username) throws EntityOperationException {
		int flag = -1;
		try {
			int nr = ((Number) entityManager.createNamedQuery("User.countAll").getSingleResult()).intValue();
			User user = new User();
			user.setId(nr);
			user.setUsername(username);
			user.setRoles(roles);
			entityManager.persist(user);
			flag = 0;
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);

		} catch (EntityExistsException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return flag;

	}

	public int remove(int id) throws EntityOperationException {
		int flag = -1;
		try {
			User user = getById(id);
			entityManager.remove(user);
			flag = 0;
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return flag;
	}

	public int update(User user) throws EntityOperationException {
		int flag = -1;
		try {
			user.setRoles(roles);
			entityManager.merge(user);
			flag = 0;
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return flag;
	}

	public int addRole(Role role) throws EntityOperationException {
		int flag = -1;
		try {
			roles.add(role);
			flag = 0;
		} catch (Exception e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return flag;
	}
}
