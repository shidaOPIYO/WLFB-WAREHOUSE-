import java.io.*;
import java.net.*;
import java.util.Scanner;


//Interactive client for the WLFB Warehouse Application.

public class kkclient {
  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 4321;

  public static void main(String[] args) {
    String host = DEFAULT_HOST;
    int port = DEFAULT_PORT;
    String role = "Client";
//if condition
    if (args.length >= 1) host = args[0];
    if (args.length >= 2) {
      try { port = Integer.parseInt(args[1]); } catch (NumberFormatException ignored) {}
    }
    if (args.length >= 3) role = args[2];

    System.out.println("Starting " + role + " connecting to " + host + ":" + port);

    try (Socket socket = new Socket(host, port);
         BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
         Scanner userIn = new Scanner(System.in)) {

      // Background thread to print server responses as they arrive
      Thread reader = new Thread(() -> {
        try {
          String line;
          while ((line = serverIn.readLine()) != null) {
            System.out.println("SERVER: " + line);
          }
        } catch (IOException e) {
          System.out.println("Connection closed by server.");
        }
      });
      reader.setDaemon(true);
      reader.start();

      System.out.println("Connected. Enter commands (CHECK, BUY APPLES n, BUY ORANGES n, ADD APPLES n, ADD ORANGES n, QUIT):");

      while (true) {
        String input = null;
        if (userIn.hasNextLine()) input = userIn.nextLine().trim();
        if (input == null || input.isEmpty()) continue;

        String upper = input.toUpperCase();
        if (upper.startsWith("BUY ") || upper.startsWith("ADD ") || upper.equals("CHECK") || upper.equals("QUIT")) {
          serverOut.println(input);
        } else {
          System.out.println("Invalid command format. Use CHECK, BUY ..., ADD ..., or QUIT.");
          continue;
        }

        if (upper.equals("QUIT")) {
          // Give server a moment to reply then exit
          try { Thread.sleep(200); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
          break;
        }
      }

    } catch (UnknownHostException e) {
      System.err.println("Unknown host: " + host);
    } catch (IOException e) {
      System.err.println("I/O error: " + e.getMessage());
    }

    System.out.println(role + " terminated.");
  }
}