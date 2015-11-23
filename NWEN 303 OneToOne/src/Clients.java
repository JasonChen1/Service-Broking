import java.util.Random;

public class Clients {

	private String message;
	private int choose = new Random().nextInt(2);// either 1 or 0

	public Clients(String message) {
		this.message = message;
		Check();
	}

	/**
	 * This check method will choose to either call post first to post a message
	 * or check first to check the message first base on the random number (1 or
	 * 0)
	 */
	private void Check() {
		// Clients can check message first(choose = 1) or it can post
		// message first(choose = 0)
		if (choose == 1) {
			doCheckFirst();
		} else {
			doPostFirst();
		}

	}

	/**
	 * This method will check the offers from the provider and see if clients
	 * request matches the providers offer if it match then it will take some
	 * time to evaluate it and remove the offer otherwise the client will post a
	 * request up to the bulletin board.
	 */
	private void doCheckFirst() {
		System.out.println("Client Checking... Offer: " + message);

		// check first
		if (Board.getProvidersMessage().contains(message)) {

			// take some time to evaluate before removing the message
			try {
				System.out.println("Client Evaluating message...");
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
			if (Board.getProvidersMessage().contains(message)) {
				System.out.println("Client found Provider's Offer: " + message);
				Board.getProvidersMessage().remove(message);
				System.out.println("Meet client's need, remove Provider's Offer: " + message);
			} else {
				System.out.println("Offer has been removed: " + message);
				Board.getClientsMessage().add(message);
				System.out.println("Client post a request: " + message);
			}

			OneToOneMatching.sem.release();

		} else {
			try {
				OneToOneMatching.sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// if message not found post it up as a request
			System.out.println("Client can not find what they need in Provider's Offers: " + message);
			Board.getClientsMessage().add(message);
			System.out.println("Client post a request: " + message);

			OneToOneMatching.sem.release();
		}

	}

	/**
	 * This method will post a message first then check if the message matches
	 * the providers message if matches then it will take some time to evaluate it
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
		// post a request first before checking it
		Board.getClientsMessage().add(message);
		System.out.println("Client post a request: " + message);

		OneToOneMatching.sem.release();

		System.out.println("Client Checking... Offer: " + message);
		// search for message that meet the clients needs from provider
		if (Board.getProvidersMessage().contains(message)) {

			// take some times to evaluate before removing the message
			try {
				System.out.println("Client Evaluating message...");
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
			if (Board.getProvidersMessage().contains(message)) {
				System.out.println("Client found Provider's Offer: " + message);
				Board.getProvidersMessage().remove(message);
				System.out.println("Meet client's need, remove Provider's Offer: " + message);
			} else {
				System.out.println("Offer has been removed: " + message);
				Board.getClientsMessage().add(message);
				System.out.println("Client post a request: " + message);
			}

			OneToOneMatching.sem.release();

		} else {
			System.out.println("Client can not find what they need in Provider's Offers: " + message);
		}

	}
}
