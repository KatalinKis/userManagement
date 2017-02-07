package managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.jboss.logging.Logger;

import exception.ManagedBeanException;
import model.User;
import util.Commons;

@Named("loginBean")
@ApplicationScoped
public class LoginManagedBean implements Serializable {
	private static final long serialVersionUID = -457433862636375912L;
	private String username;
	private boolean exception;

	public boolean login() throws ManagedBeanException {
		UserManagedBean umb = new UserManagedBean();
		boolean flag = false;
		List<?> users = new ArrayList();
		try {
			users = umb.getAll();
		} catch (ManagedBeanException e) {
			exception = true;
			Commons.getInstance().throwException(Commons.getInstance().INTERNAL_ERROR);
		}

		for (int i = 0; i < users.size(); ++i) {
			if (users.get(i).getClass().equals(User.class)) {
				String u_name = ((User) users.get(i)).getUsername();
				if (u_name.equals(username)) {
					flag = true;
				}
			}
		}
		if (!flag) {
			Commons.getInstance().throwException(Commons.getInstance().INVALID_LOGIN);
		}
		return flag;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isException() {
		return exception;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}

}
