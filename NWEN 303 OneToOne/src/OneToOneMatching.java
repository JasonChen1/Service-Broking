
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class OneToOneMatching {
	// fair semaphore may cause busy wait
	public static Semaphore sem = new Semaphore(1, true);
	public static final int evaluateTime = 0;
	public static final int postPeriod = 0;
	private static int cycles = 20;
	
	public OneToOneMatching(){
		
	}
	
	public static void main(String[] args) {

		StopWatch timer = new StopWatch();

		ExecutorService executor = Executors.newCachedThreadPool();

		timer.start();
		// each time goes through the loop it will create 2 threads one running
		// the clients class and one running the providers class
		for (int i = 0; i < cycles; i++) {
			// Variables to hold the random string of message (0-4) each time go
			// through the loop
			final String requests = "" + new Random().nextInt(5);
			final String offers = "" + new Random().nextInt(5);

			// will wait for a period of time each time clients/providers start
			// posting message up
			try {
				Thread.sleep(postPeriod);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			
			// each time go through the loop it will create a new thread to run
			// the clients and a new thread to run providers
			executor.submit(new Runnable() {
				public void run() {
					new Clients(requests);
				}
			});
			executor.submit(new Runnable() {
				public void run() {
					new Providers(offers);
				}
			});
			
			System.out.println("\n");
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
