//A Class that represents use-defined exception 
public class MyException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4907306689117781753L;

	public MyException(String s, Throwable cause) 
    { 
        // Call constructor of parent Exception 
        super(s, cause); 
    } 
} 


