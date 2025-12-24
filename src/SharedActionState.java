import java.net.*;
import java.io.*;

public class SharedActionState{
	
	private int apples;
	private int oranges;
	
	private SharedActionState mySharedObj;
	private String myThreadName;
	private double mySharedVariable;
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers

// Constructor	
	
	SharedActionState(int initialapples, int initialoranges) {
		this.apples = initialapples;
		this.oranges = initialoranges;
		
	}

//Attempt to aquire a lock
	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        System.out.println(me.getName()+" is attempting to acquire a lock!");	
	        ++threadsWaiting;
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    --threadsWaiting;
		    accessing = true;
		    System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		  }
	
	
    /* The processInput method */

	public synchronized String processInput(String myThreadName, String theInput) {
    		System.out.println(myThreadName + " received "+ theInput);
    		String theOutput = null;
    		// Check what the client said
    		
    		//spilt the input into two part the command and quantity which are separated by space
    		//the command is a string 
    		//the quantity will be converted from string to integer
    		String parts[] = theInput.trim().split("\\s+");
    		
    		if (parts.length == 0) {
    			return "invalid input, empty command";
    		}
    		
    		//The first part of the split input is the command
    		String command = parts[0].toUpperCase();
    		
    		switch (command) {
    			case "MENU":
    				return "Functionalities : CHECK_STOCK | BUY_APPLES n | BUY_ORANGES n | ADD_APPLES n | ADD_ORANGES n | QUIT";
    				
    			case "CHECK_STOCK":
    				return "STOCK: Apples = " + apples + "Oranges = " + oranges;
    			
    			case "BUY_APPLES":
    				
    			case "BUY_ORANGES":
    				
    			case "ADD_APPLES":
    			
    			case "ADD_ORANGES":
    				
    			case "QUIT":
    				return "goodbye";
    				
    			default :
    				return "Wrong input, Type Menu";

    		}
    		
    		private String buyApples(String thread, String[] parts) {
    			if (parts.length != 2 ){
    				return "Invalid input: BUY_APPLES n (replace n with quantity you want";
    			}
    			
    			try {
    				int quantity = Integer.parseInt(parts[1]);
    				if (quantity <= 0) {
    					return "Invalid quantity, quantity should be greater than 0";
    				}
    				
    				if (quantity > apples) {
    					return "ERROR! Apples are not enough. Current Apples = " + apples;
    				}else {
    					apples -= quantity;
    					return  quantity + " apples purchased successfully. Remaining apples= " + apples;
    				}
    				
    				
    			} catch (Exception e ) {
    				return "ERROR! Invalid number";
    			}
    		}
    		
    		private String buyOranges(String thread, String[] parts) {
    			if (parts.length != 2 ){
    				return "Invalid input: BUY_ORANGES n (replace n with quantity you want";
    			}
    			
    			try {
    				int quantity = Integer.parseInt(parts[1]);
    				if (quantity <= 0) {
    					return "Invalid quantity, quantity should be greater than 0";
    				}
    				
    				if (quantity > oranges) {
    					return "ERROR! Oranges are not enough. Current Oranges = " + oranges;
    				}else {
    					oranges -= quantity;
    					return  quantity + " oranges purchased successfully. Remaining oranges= " + oranges;
    				}
    				
    				
    			} catch (Exception e ) {
    				return "ERROR! Invalid number";
    			}
    			
    		}
    		
    		private String addApples(String thread, String[] parts) {
    			if (parts.length != 2 ){
    				return "Invalid input: ADD_APPLES n (replace n with quantity you want";
    			}
    			
    			try {
    				int quantity = Integer.parseInt(parts[1]);
    				if (quantity <= 0) {
    					return "Error! quanitity must be greater than zero!";
    					
    				}
    				
    				apples += quantity;
    				
    				return "OK: " + quantity + " apples added successfully. Current apples = " + apples ;  
    				
    				
    			} catch (Exception e) {
    				return "ERROR! Invalid input" ;
    			}
    			
    			
    		}
    		
    		private String addOranges(String thread, String[] parts) {
    			if (parts.length != 2 ){
    				return "Invalid input: ADD_ORANGES n (replace n with quantity you want";
    			}
    			
    			try {
    				int quantity = Integer.parseInt(parts[1]);
    				if (quantity <= 0) {
    					return "Error! quanitity must be greater than zero!";
    					
    				}
    				
    				ORANGES += quantity;
    				
    				return "OK: " + quantity + " oranges added successfully. Current oranges = " + oranges ;  
    				
    				
    			} catch (Exception e) {
    				return "ERROR! Invalid input" ;
    			}
    			
    			
    			
    		}
    		
    		
    		
    		
    		if (theInput.equalsIgnoreCase("Do my action!")) {
    			//Correct request
    			if (myThreadName.equals("ActionServerThread1")) {
    				/*  Add 20 to the variable
    					multiply it by 5
    					divide by 3.
    				 */
    				mySharedVariable = mySharedVariable + 20;
       				mySharedVariable = mySharedVariable * 5;
       				mySharedVariable = mySharedVariable / 3;
   				System.out.println(myThreadName + " made the SharedVariable " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;
    			}
    			else if (myThreadName.equals("ActionServerThread2")) {
    				/*	Subtract 5 from the variable
    					Multiply it by 10 
    					Divide by 2.5
    					*/
       				mySharedVariable = mySharedVariable - 5;
       				mySharedVariable = mySharedVariable * 10;
       				mySharedVariable = mySharedVariable / 2.5;
    					
    				System.out.println(myThreadName + " made the SharedVariable " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;

    			}
       			else if (myThreadName.equals("ActionServerThread3")) {
       				/*	Subtract 50
						Divide by 2
						Multiply by 33
       				 */
       				mySharedVariable = mySharedVariable - 50;
       				mySharedVariable = mySharedVariable / 2;
       				mySharedVariable = mySharedVariable * 33;
 
       				System.out.println(myThreadName + " made the SharedVariable " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;

       			}
       			else if (myThreadName.equals("ActionServerThread4")) {
    				/*	Multiply by 20
						Divide by 10
						Subtract 1
    				 */
       				mySharedVariable = mySharedVariable * 20;
       				mySharedVariable = mySharedVariable / 10;
       				mySharedVariable = mySharedVariable - 1;
    				System.out.println(myThreadName + " made the SharedVariable " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;
       			}
       			else {System.out.println("Error - thread call not recognised.");}
    		}
    		else { //incorrect request
    			theOutput = myThreadName + " received incorrect request - only understand \"Do my action!\"";
		
    		}
 
     		//Return the output message to the ActionServer
    		System.out.println(theOutput);
    		return theOutput;
    	}	
}

