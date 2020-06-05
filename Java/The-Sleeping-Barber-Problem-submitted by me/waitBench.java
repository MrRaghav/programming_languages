import java.util.concurrent.ArrayBlockingQueue;

public class waitBench {

    private final ArrayBlockingQueue<Customer> waitCustomers;

    public waitBench(int capacity) {
    	waitCustomers = new ArrayBlockingQueue<>(capacity);
    }
  
    /*public void sizeRoom() {
    	waitingCustomers.size();
    	System.out.println("available seats are " + waitingCustomers.size());
    	return;
    }*/
    
    public void sitOnBench(Customer customer) throws InterruptedException {
    	waitCustomers.put(customer);
    }

    public Customer nextCustomer() throws InterruptedException {
        return waitCustomers.take();
    }

}
