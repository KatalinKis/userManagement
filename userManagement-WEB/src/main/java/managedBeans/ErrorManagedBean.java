package managedBeans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("errorBean")
@ApplicationScoped
public class ErrorManagedBean {
	private String errorMessage;
	private static ErrorManagedBean errorManagedBean;

	private ErrorManagedBean(){
		
	}
	public static ErrorManagedBean getErrorBean(){	
		if (errorManagedBean == null){
			errorManagedBean = new ErrorManagedBean();
		}
		return errorManagedBean;
	}
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMess) {
		this.errorMessage = errorMess;
	}
}
