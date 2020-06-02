
import java.util.concurrent.Callable;

public class Barber implements Callable<Customer> {

    private final waitBench waitBench;
    
    /**
     * Constructor for class Barber @param waitingRoom
     */
    public Barber(waitBench waitBench) {
        this.waitBench = waitBench;
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public Customer call() {
        try {
            while (true) {
                Customer customer = waitBench.nextCustomer();
                String threadName = Thread.currentThread().getName(); //reference from lecture notes
                System.out.println("Barber number" + threadName.substring(13) + " has called the customer " + customer +" for the hair cut!");
                customer.callForHairCut();
                customer.releaseCustomerAfterServe(customer);
            }

        } catch (InterruptedException e) {
        	String threadName = Thread.currentThread().getName(); //reference from lecture notes
        	System.out.println();
            System.out.println("Barber number" + threadName.substring(13) + " has finished his job");
        } catch (MyException e) {
			e.printStackTrace();
		}
		return null;
    }
}
