import java.net.*;
import java.io.*;



//Warehouse server for WLFB Warehouse Application.
//listens on port

public class kkmsserver {
  public static final int PORT = 4321;

  public static void main(String[] args) {
    InetAddress computerAddr = null;
    try {
      computerAddr = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      System.out.println("Unable to determine local host: " + e.getMessage());
    }
    System.out.println("Server host: " + (computerAddr != null ? computerAddr.getHostName() : "unknown"));

    ServerSocket serverSocket = null;
    
    
    // creates a warehouse state with initial stock (1000 apples, 1000 oranges)
    kkstate warehouse = new kkstate(1000, 1000);

    try {
      serverSocket = new ServerSocket(PORT);
      System.out.println("Warehouse server started on port " + PORT);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        
        // Passes  the shared warehouse state to each handler
        new kkmsthread(clientSocket, warehouse).start();
        System.out.println("New server thread started for " + clientSocket.getRemoteSocketAddress());
      }
    } catch (IOException e) {
      System.err.println("Server I/O error: " + e.getMessage());
    } finally {
      if (serverSocket != null && !serverSocket.isClosed()) {
        try { serverSocket.close(); } catch (IOException ignored) {}
      }
    }
  }
}