
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer extends Exception implements Callable<Customer> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final AtomicInteger idGenerator = new AtomicInteger();

	private final waitBench waitBench;
	
	private final int id;

    private final SynchronousQueue<Boolean> synchronousQueue;

    private volatile boolean hairCut;
    
    static int numberOfFreeSeatsLocal;

    /**
     * Constructor for Class Customer @param waitBench
     */
    //public Customer(waitBench waitBench, int numberOfFreeSeats) {
    public Customer(waitBench waitBench) {
        this.id = idGenerator.incrementAndGet();
        this.waitBench = waitBench;
        //waitBench.sizeRoom();
        //numberOfFreeSeatsLocal = numberOfFreeSeats;
        this.synchronousQueue = new SynchronousQueue<>();
    }
    
    
    /*public Customer leaves() {
    	System.out.println("number of fee seats local" + numberOfFreeSeatsLocal);
    	synchronized(this) {
    		if( numberOfFreeSeatsLocal == 0)
    		{
    			System.out.println("CUSTOMER " + this + "has LEFT due to no seat");  			//no chair available for customer
    			return null;
    		}
    	}
		return null;	
    }
    */

    /* (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public Customer call() {
       //while(true) {
    	try {
        	
    		synchronized(this) {                          //blocked synchronization
        	//if(numberOfFreeSeatsLocal>=1) {
        	//waitBench.sizeRoom();
        	//leaves();
    		//synchronousQueue.
        	try {
				waitBench.sitOnBench(this);
			} catch (InterruptedException e) {
				 e.printStackTrace();
			}
            System.out.println("Customer number-" + this + " is waiting for the hair cut!");
            //numberOfFreeSeatsLocal--;
           // System.out.println("take a seat - left seats are " + numberOfFreeSeatsLocal);
            try {
            	waitToBeCalledForHaircut();
			} catch (MyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            hairCut = true;
            //numberOfFreeSeatsLocal++;
            //System.out.println("wait to be called - left seats are " + numberOfFreeSeatsLocal);
        	//}
        	//else {
        	//	System.out.println("CUSTOMER " + this + "has LEFT due to no seat");
        } //end of synchronize
            
        //} /*catch (Exception e) {
            //e.printStackTrace();
       } 
        catch (IllegalStateException e) {

        	System.out.println("There are NO free seats. Customer " + this.id + " has LEFT the barbershop.");
        	}
    	//break;
       //}
		return null;
    }

    Random rand = new Random();
    double customerRandomSleepTime = rand.nextGaussian();{

    while(customerRandomSleepTime<=0.0) {
	           customerRandomSleepTime = rand.nextGaussian();
	           //System.out.println("random sleep time for customer is" + customerRandomSleepTime);
	          //numRandomCustomers = rand.nextInt(10);
              }
    }
     int customerSleepTime = (int) Math.round(customerRandomSleepTime);
     int customerFinalSleepTime = customerSleepTime*1000;
     
//     ("customer sleep time is " + customerSleepTime);
    
    /**
     * @throws MyException
     */
    public void callForHairCut() throws MyException {
        try {
			synchronousQueue.put(true);
			try {
				Thread.sleep(customerSleepTime); //for starvation
				  //TimeUnit.SECONDS.sleep(1);					    
			} catch (InterruptedException e) {
				e.printStackTrace();					
			}	
			
			//numberOfFreeSeatsLocal++;
			//System.out.println("left seats are " + numberOfFreeSeatsLocal);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void releaseCustomerAfterServe(Customer customer) {
    	System.out.println("customer number-" + customer + " has been served and left the shop!");
    }

    /**
     * @throws MyException
     */
    public void waitToBeCalledForHaircut() throws MyException {
        try {
			synchronousQueue.take();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
    }

    public boolean isHairCut() {
        return hairCut;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    
}
