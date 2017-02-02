package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import common.RolesManagementInterface;
import common.UserManagementInterface;
import model.Role;
import model.User;

@Stateless
public class RoleBean implements RolesManagementInterface {

	@PersistenceContext(unitName = "userManagement-JPA")
	private EntityManager entityManager;

	private Role getById(int id) {
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
}
