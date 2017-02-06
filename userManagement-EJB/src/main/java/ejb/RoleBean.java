package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import common.RolesManagementInterface;
import exception.EntityOperationException;
import model.Role;
import model.User;

@Stateless
public class RoleBean implements RolesManagementInterface {

	@PersistenceContext(unitName = "userManagement-JPA")
	private EntityManager entityManager;
	private List<User> users = new ArrayList<User>();
	private Logger oLogger = Logger.getLogger(RoleBean.class);

	public Role getById(int id) throws EntityOperationException {
		Role role = null;
		try {
			role = entityManager.find(Role.class, id);
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return role;
	}

	public int add(String user_role) throws EntityOperationException {
		int flag = -1;
		try {
			int nr = ((Number) entityManager.createNamedQuery("Role.countAll").getSingleResult()).intValue();
			Role role = new Role();
			role.setId(nr);
			role.setRole(user_role);
			entityManager.persist(role);
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

	public int addUser(User user) throws EntityOperationException {
		int flag = -1;
		try {
			users.add(user);
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
		Role role = null;
		try {
			role = getById(id);
			entityManager.remove(role);
			flag = 0;
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		} catch (EntityOperationException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return flag;
	}

	public int update(Role role) throws EntityOperationException {
		int flag = -1;
		try {
			entityManager.merge(role);
			flag = 0;
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return flag;

	}

	public List<Role> getAllRoles() throws EntityOperationException {
		List<Role> roles = new ArrayList<Role>();
		try {
			roles = entityManager.createNamedQuery("Role.findAll").getResultList();
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new EntityOperationException("Entity exception caught.", e);
		}
		return roles;
	}

	public Role searchRole(String message) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteria = cb.createQuery(Role.class);
		Root<Role> member = criteria.from(Role.class);

		// criteria.select(member).where(cb.like(member.get("role"), "%" +
		// message + "%"));
		return entityManager.createQuery(criteria).getSingleResult();
	}
}
