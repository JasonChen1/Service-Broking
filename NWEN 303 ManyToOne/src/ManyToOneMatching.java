

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ManyToOneMatching {
	// fair semaphore may cause busy wait
	public static Semaphore sem = new Semaphore(1, true);
	// clients sign up limit
	private static final int maxOffers = 5;
	// either 1 or 0 use to decide whether the offer should be time limit
	private static int choose;
	private static int timeLimit = 300; // 1 second
	private static final int cycles = 50;
	// number of threads to run clients each cycle
	private static final int threads = 5;
	
	public ManyToOneMatching() {

	}

	/**
	 * This method will go through each providers message and find the message
	 * that matches the clients request if it matches it will add the message
	 * into the providers list if the list is full it will remove the list. if
	 * the message is not found it will post a request up to the board.
	 * 
	 * @param message
	 */
	private static void ClientSignUp(String message) {
		// check if any offer matches the clients message
		if (Board.getProvidersMessage().contains(message)) {

			try {
				ManyToOneMatching.sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// have a chance to cancel the request of the clients 1/5 chance
			int cancelRate = new Random().nextInt(5);
			// has a chance to cancel the request
			if (cancelRate == 1 && Board.getClientsMessage().contains(message)) {
				Board.getClientsMessage().remove(message);
				System.out.println("Client has cancel the request: " + message);
			}

			// go through the providers and one that matches the message the
			// client send
			for (int i = 0; i < Board.getProviders().size(); i++) {
				if (Board.getProviders().get(i).toString().equals(message)) {
					// make sure the providers list is not full and the provider
					// contains the message
					if (Board.getProviders().get(i).getProvidersList().size() < maxOffers
							&& Board.getProvidersMessage().contains(message)) {
						// sign up for that offer if client find the offer
						// they want
						Board.getProviders().get(i).getProvidersList().add(message);
						System.out.println("Client sign up for offer: " + message);

						// check right away after a client sign up to see if the
						// list is full
						if (Board.getProviders().get(i).getProvidersList().size() == maxOffers
								&& Board.getProvidersMessage().contains(message)) {
							Board.getProvidersMessage().remove(message);
							System.out.println("Required number of clients has sign up, remove the offer: " + message);
						}
					}
				} else if (Board.getProviders().get(i).getProvidersList().size() == maxOffers
						&& Board.getProvidersMessage().contains(message)) {
					// double check
					Board.getProvidersMessage().remove(message);
					System.out.println("Required number of clients has sign up, remove the offer: " + message);
				}
			}

			ManyToOneMatching.sem.release();

		} else {
			try {
				ManyToOneMatching.sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// double check (normally should just jump to else clause)
			if (Board.getProvidersMessage().contains(message)) {
				for (int i = 0; i < Board.getProviders().size(); i++) {
					if (Board.getProviders().get(i).toString().equals(message)) {
						// make sure the providers list is not full and the
						// provider contains the message
						if (Board.getProviders().get(i).getProvidersList().size() < maxOffers
								&& Board.getProvidersMessage().contains(message)) {
							// sign up for that offer if client find the offer
							// they want
							Board.getProviders().get(i).getProvidersList().add(message);
							System.out.println("Client sign up for offer: " + message);
							// check right away after a client sign up to see if
							// the list is full
							if (Board.getProviders().get(i).getProvidersList().size() == maxOffers
									&& Board.getProvidersMessage().contains(message)) {
								Board.getProvidersMessage().remove(message);
								System.out.println(
										"Required number of clients has sign up, remove the offer: " + message);
							}
						}
					}
				}
			} else {
				// if message not found post it up as a request
				Board.getClientsMessage().add(message);
				System.out.println("Client post a request: " + message );
			}

			ManyToOneMatching.sem.release();
		}
	}

	/**
	 * This method will add a providers offer first then it will check the
	 * clients message and see if any message matches if so it will call
	 * providercheck() method which will add the clients message into the
	 * providers list and remove it from the clients list
	 * 
	 * @param message
	 */
	private static void ProvidersOffer(String message) {
		int count = 0;

		try {
			ManyToOneMatching.sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// post an offer
		Board.getProviders().add(new Providers(message));
		Board.getProvidersMessage().add(message);
		System.out.println("Provider post an offer: " + message );
		providerCheck(count, message);

		ManyToOneMatching.sem.release();

	}

	/**
	 * This method will first add the provider then it will check in the client
	 * list and set a timer which will remove the offer when the time is reach
	 * 
	 * @param message
	 */
	private static void timeLimitOffer(String message) {
		int count = 0;

		try {
			ManyToOneMatching.sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// post an offer
		Board.getProviders().add(new Providers(message));
		Board.getProvidersMessage().add(message);
		System.out.println("Provider post an offer: " + message );
		ManyToOneMatching.sem.release();

		synchronized (message) {
			providerCheck(count, message);
		}
		synchronized (message) {
		// every offer will have a time limit
		try {
			Thread.sleep(timeLimit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// if the time limit is up it will remove the offer no matter how many
		// clients has sign up for that offer
		
			if (Board.getProvidersMessage().contains(message)) {
				Board.getProvidersMessage().remove(message);
				Board.getProviders().remove(message);
				System.out.println("Time limit is up, Provider has remove offer: " + message);
			}
		}
	}

	/**
	 * checking part for providers use to check if client contain the request
	 * that the provider want
	 */
	private static void providerCheck(int count, String message) {
		// have a chance to cancel the request of the clients 1/5 chance
		int cancelRate = new Random().nextInt(5);
		// has a chance to cancel the request
		if (cancelRate == 1 && Board.getProvidersMessage().contains(message)) {
			Board.getProvidersMessage().remove(message);
			Board.getProviders().remove(message);
			System.out.println("Provider has cancel the offer: " + message);
		}

		if (Board.getProvidersMessage().contains(message)) {
			// while provider seeing a request they can fulfill it will add
			// it to their list and remove the request from client after it has
			// been added
			while (Board.getClientsMessage().contains(message) && maxOffers >= count) {
				for (int i = 0; i < Board.getProviders().size(); i++) {
					if (Board.getProviders().get(i).toString().equals(message)
							&& Board.getProviders().get(i).getProvidersList().size() < maxOffers) {
						// add the request to the providers list and remove
						// the clients message
						Board.getProviders().get(i).getProvidersList().add(message);
						Board.getClientsMessage().remove(message);
						System.out.println("Add client to the provider's list and remove the request: " + message);
						// check right away after a client sign up to see if
						// the list is full
						if (Board.getProviders().get(i).getProvidersList().size() == maxOffers) {
							Board.getProvidersMessage().remove(message);
							System.out.println("Required number of clients has sign up, remove the offer: " + message);
						}

					}
				}
				count++;
			}
		}
	}

	/**
	 * use to choose whether the offer should be time limit or not 1 - no limit
	 * 0- time limit
	 */
	private static void check(String message) {
		if (choose == 1) {
			ProvidersOffer(message);
		} else {
			timeLimitOffer(message);
		}
	}

	public static void main(String[] args) {
		
		StopWatch timer = new StopWatch();
		timer.start();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		// each time go through the loop it will create multiple threads to
		// run the clients and a new thread to run providers
		for (int i = 0; i < cycles; i++) {
			choose = new Random().nextInt(2);
			executor.submit(new Runnable() {
				// generate a random number form 0-4 each time a new thread is
				// created
				final String offers = "" + new Random().nextInt(5);
				public void run() {
					check(offers);
				}
			});

			for (int j = 0; j < threads; j++) {
				// generate a random number form 0-4 each time a new thread is
				// created
				final String requests = "" + new Random().nextInt(5);
				executor.submit(new Runnable() {
					public void run() {
						ClientSignUp(requests);
					}
				});
			}
		}
		
		executor.shutdown();// terminate when thread finish running
		
		// otherwise it will terminate after 5 minutes
		try {
			executor.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timer.stop();
		System.out.println("\ntiming: "+timer.getElapsedTime());
	}

}
