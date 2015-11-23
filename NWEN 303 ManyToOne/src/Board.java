
import java.util.ArrayList;

public class Board {
	// Arrays to hold the messages (board)
	private static ArrayList<String> clientsMessage = new ArrayList<String>();
	private static ArrayList<String> providersMessage = new ArrayList<String>();
	private static ArrayList<Providers> providers = new ArrayList<Providers>();
	private static ArrayList<Clients> clients = new ArrayList<Clients>();
	
	
	public Board() {

	}

	public static ArrayList<String> getClientsMessage() {
		return clientsMessage;
	}

	public static ArrayList<String> getProvidersMessage() {
		return providersMessage;
	}

	public static ArrayList<Providers> getProviders() {
		return providers;
	}

	public static ArrayList<Clients> getClients() {
		return clients;
	}


}
