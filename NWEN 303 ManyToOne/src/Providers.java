

import java.util.ArrayList;
import java.util.List;

public class Providers {

	private  List<String> providersList = new ArrayList<String>();
	private String message;
	
	public Providers(String message) {
		this.setMessage(message);
		
	}

	public List<String> getProvidersList() {
		return providersList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return  message;
	}
	
}
