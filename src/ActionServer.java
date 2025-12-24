import java.net.*;
import java.io.*;



public class ActionServer {
  public static void main(String[] args) throws IOException {

	ServerSocket ActionServerSocket = null;
    boolean listening = true;
    String ActionServerName = "ActionServer";
    int ActionServerNumber = 4545;
    
    //initial warehouse stocks
    int initialApples = 1000;
    int initialOranges = 1000;
    
  
    //Create the shared object in the global scope... 
    //Creates the shared warehouse state
    
    SharedActionState ourSharedActionStateObject = new SharedActionState(initialApples, initialOranges);
        
    // Make the server socket

    try {
      ActionServerSocket = new ServerSocket(ActionServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + ActionServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(ActionServerName + " started");

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerCustomerAThread", ourSharedActionStateObject).start();
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerCustomerBThread", ourSharedActionStateObject).start();
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerSupplierThread", ourSharedActionStateObject).start();
      System.out.println("New " + ActionServerName + " thread started.");
    }
    ActionServerSocket.close();
  }
}