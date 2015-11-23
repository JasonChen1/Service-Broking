

public class Clients {
	private String message;
	
	public Clients(String message){
		this.setMessage(message);
		
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
