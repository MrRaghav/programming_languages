
//import java.util.concurrent.Callable;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.toList;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.lang.management.ManagementFactory;
//import java.lang.management.ThreadInfo;
//import java.lang.management.ThreadMXBean;
import java.util.stream.Stream;
import java.util.Scanner;
import java.util.HashSet;
//import java.util.InputMismatchException;
//import java.util.Date;
import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//import java.lang.management;
import java.util.List;
import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
//import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.Set;
//import java.io.*;

public class Main {
	
    /*** @param args * @throws MyException   */

    public static void main(String[] args) throws MyException {
        
    	Scanner in = new Scanner(System.in);

     	int userInput = 0;
        int numBarbers=0, numWaitBench =0;
        int numCustomers=0;
        //int numRandomCustomers = ThreadLocalRandom.current().nextInt();
        Random rand = new Random();
        
        //double numRandomCustomers = rand.nextGaussian();
        //double numRandomCustomers = 3.6;
        //System.out.println("random customer is" + numRandomCustomers);
        int numRandomCustomers = rand.nextInt(500);
        while(numRandomCustomers<=0) {
        	//numRandomCustomers = rand.nextGaussian();
        	//System.out.println("random customer is" + numRandomCustomers);
        	numRandomCustomers = rand.nextInt(500);
        }
        //System.out.println(numRandomCustomers);
        
    	System.out.println("...Welcome...");
    	System.out.println("New barber shop has opened on -" + java.time.LocalDate.now() + "!!!");
    	System.out.println("Let me help you with the quick setup!");
    	System.out.println("Please answer below questions to proceed!");
    	System.out.println();
    	try {
    	    System.out.println("How many barbers (M) do you want in your barber shop?");
    	    numBarbers = Integer.parseInt(in.nextLine());
    	
    	    System.out.println("How many waiting seats (N) do you want in your barber shop?");
    	    numWaitBench= Integer.parseInt(in.nextLine());
    	
    	    System.out.println("If you want to specify number of customers, press 1");
    	    System.out.println("Otherwise, press 2 - this will assign random number of customers to this shop!");
    	
    	    userInput = Integer.parseInt(in.nextLine());
    	
    	    switch(userInput) {
    	    case 1:
        	     System.out.println("How many customers do you want?");
        	     numCustomers = Integer.parseInt(in.nextLine());
        	     break;

    	    case 2:
    		     numCustomers = (int) Math.round(numRandomCustomers);
    		     //numCustomers = numRandomCustomers;
    		     System.out.println("Alright, we can say " + numCustomers + " customers will visit now!");
    		     break;
    		
    	     default:
    		     System.out.println("Only 1 or 2 is acceptable as input. Please restart the program! Exiting...");
    		     System.exit(0);
    	     }
    	
    	}catch (NumberFormatException e)
    	{
    		System.out.println("Unexpected key was pressed. Please restart the program and enter the correct number! Exiting...");
    		System.exit(0);

    	}
    	   	
    	
        Set s= new HashSet<>();
        
    	waitBench waitBench = new waitBench(numWaitBench);
        //Internally manages thread pool of numOfCustomers threads
        ExecutorService executorService = Executors.newFixedThreadPool(numCustomers);
        
        //Internally manages thread pool for no of Barber
        for(int i=0;i<numBarbers;i++)
        {
        	Future<Customer> result = executorService.submit(new Barber(waitBench));
            s.add(result);
            //System.out.println(s);
        }
        
        //int numberOfFreeSeats = numWaitBench;
        //System.out.println("number of free seats for the customers are " + numberOfFreeSeats);
        //List<Customer> customers = Stream.generate(() -> new Customer(waitBench, numberOfFreeSeats)).limit(numCustomers).peek(executorService::submit).collect(toList());
        List<Customer> customers = Stream.generate(() -> new Customer(waitBench)).limit(numCustomers).peek(executorService::submit).collect(toList());
        //Customer customers = new Customer(waitBench, numberOfFreeSeats);
        
        while (!customers.stream().allMatch(Customer::isHairCut)) {
        //while (!customers.isHairCut()) {
				try {
					  Thread.sleep(1000); //for starvation
					  //TimeUnit.SECONDS.sleep(1);					    
				} catch (InterruptedException e) {
					e.printStackTrace();					
				}			
        }

        executorService.shutdownNow();       //closing the executor service
        in.close();
			try {
				executorService.awaitTermination(1, MINUTES);
		        //System.gc(); //not required right now
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
		System.out.println();
		System.out.println("All customers have been served!!!");
	    System.out.println("Shop is closed now!");
    }

}
