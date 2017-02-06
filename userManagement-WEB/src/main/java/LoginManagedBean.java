import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import exception.ManagedBeanException;
import model.User;

@Named("loginBean")
@ApplicationScoped
public class LoginManagedBean implements Serializable{
	private static final long serialVersionUID = -457433862636375912L;
	private String username;
	private boolean exception;
	

	public boolean login() throws ManagedBeanException{
		UserManagedBean umb = new UserManagedBean();
		List<?> users;
		try {
			users = umb.getAll();
		} catch (ManagedBeanException e) {
			exception = true;
			throw new ManagedBeanException("Internal error!", e);
		}
		
		for (int i = 0; i < users.size(); ++i){
			if (users.get(i).getClass().equals(User.class)){
				if (((User)users.get(i)).getUsername().equals(username)){
					return true;
				}
			}
		}
		return false;
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
