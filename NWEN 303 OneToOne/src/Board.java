import java.util.ArrayList;

public class Board {
	// Arrays to hold the messages (board)
	private static ArrayList<String> clientsMessage = new ArrayList<String>();
	private static ArrayList<String> providersMessage = new ArrayList<String>();
	
	public Board() {

	}

	public static ArrayList<String> getClientsMessage() {
		return clientsMessage;
	}

	public static ArrayList<String> getProvidersMessage() {
		return providersMessage;
	}

}
