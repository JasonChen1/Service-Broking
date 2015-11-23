import java.util.Random;

public class Providers {

	private String message;
	private int choose = new Random().nextInt(2);// either 1 or 0

	public Providers(String message) {
		this.message = message;
		Check();
	}

	/**
	 * This check method will choose to either call post first to post a message
	 * or check first to check the message first base on the random number (1 or
	 * 0)
	 */
	private void Check() {
		// Provider can check message first(choose = 1) or it can post
		// message first(choose = 0)
		if (choose == 1) {
			doCheckFirst();
		} else {
			doPostFirst();
		}

	}

	/**
	 * This method will check the request of the client and see if provider's
	 * offer matches the client's request if it match then it will take some
	 * time to evaluate it and remove the request otherwise the provider will
	 * post an offer up to the bulletin board.
	 */
	private void doCheckFirst() {
		System.out.println("Provider Checking... Request: " + message);

		// check first
		if (Board.getClientsMessage().contains(message)) {

			// take some time to evaluate before removing the message
			try {
				System.out.println("Provider Evaluating message...");
				Thread.sleep(OneToOneMatching.evaluateTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				OneToOneMatching.sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// after evaluating an offer or request, it will
			// check that it is still valid before removing it.
			if (Board.getClientsMessage().contains(message)) {
				System.out.println("Provider found Client's Request: " + message);
				Board.getClientsMessage().remove(message);
				System.out.println("Provider can fulfil Client's Request, Remove Client's Request: " + message);
			} else {
				System.out.println("Request has been removed: " + message);
				Board.getProvidersMessage().add(message);
				System.out.println("Provider post an offer: " + message);
			}

			OneToOneMatching.sem.release();
		} else {
			try {
				OneToOneMatching.sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// adding message if not found
			System.out.println("Provider can not find any Client's Request to fulfil: " + message);
			Board.getProvidersMessage().add(message);
			System.out.println("Provider post an offer: " + message);

			OneToOneMatching.sem.release();
		}

	}

	/**
	 * This method will post a message first then check if the message matches
	 * the clients message if matches then it will take some time to evaluate it
	 * and remove the message otherwise it will post a message up to the
	 * bulletin board
	 */
	private void doPostFirst() {
		// post first
		try {
			OneToOneMatching.sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// post message first before checking it
		Board.getProvidersMessage().add(message);
		System.out.println("Provider post an offer: " + message);

		OneToOneMatching.sem.release();

		System.out.println("Provider Checking... Request: " + message);
		// search for message that meet the clients needs from provider
		if (Board.getClientsMessage().contains(message)) {

			// take some time to evaluate before removing the message
			try {
				System.out.println("Provider Evaluating message...");
				Thread.sleep(OneToOneMatching.evaluateTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				OneToOneMatching.sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// after evaluating an offer or request, it will
			// check that it is still valid before removing it.
			if (Board.getClientsMessage().contains(message)) {
				System.out.println("Provider found Client's Request: " + message);
				Board.getClientsMessage().remove(message);
				System.out.println("Provider can fulfil Client's Request, Remove Client's Request: " + message);
			} else {
				System.out.println("Request has been removed: " + message);
				Board.getProvidersMessage().add(message);
				System.out.println("Provider post an offer: " + message);
			}

			OneToOneMatching.sem.release();

		} else {
			System.out.println("Provider can not find any Client's Request to fulfil: " + message);
		}

	}
}
