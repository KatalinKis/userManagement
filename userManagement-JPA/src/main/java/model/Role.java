package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the roles database table.
 * 
 */
@Entity
@Table(name = "roles")
@NamedQueries({ @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r order by r.id"),
		@NamedQuery(name = "Role.countAll", query = "select count(r) +1 from Role r")})
public class Role implements Serializable, IEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String role;

	// bi-directional many-to-many association to User
	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	public Role() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}