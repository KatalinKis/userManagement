package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import common.RolesManagementInterface;
import model.Role;
import model.User;

@Stateless
public class RoleBean implements RolesManagementInterface {

	@PersistenceContext(unitName = "userManagement-JPA")
	private EntityManager entityManager;
	private List<User> users = new ArrayList<User>();

	public Role getById(int id) {
		return entityManager.find(Role.class, id);
	}

	public int add(String user_role) {
		int nr = ((Number) entityManager.createNamedQuery("Role.countAll").getSingleResult()).intValue();
		Role role = new Role();
		role.setId(nr);
		role.setRole(user_role);
		entityManager.persist(role);
		return 0;
	}

	public int addUser(User user) {
		users.add(user);
		return 0;
	}

	public int remove(int id) {
		Role role = getById(id);
		entityManager.remove(role);
		return 0;
	}

	public int update(Role role) {
		entityManager.merge(role);
		return 0;
	}

	public List<Role> getAllRoles() {
		List<Role> roles = entityManager.createNamedQuery("Role.findAll").getResultList();
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
