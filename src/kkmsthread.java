import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


//Handles a single client connection. Uses a shared kkstate instance for all operations.
 
public class kkmsthread extends Thread {
  private final Socket socket;
  private final kkstate warehouse;
  private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

  public kkmsthread(Socket socket, kkstate warehouse) {
    super("kkmsthread-" + socket.getPort());
    this.socket = socket;
    this.warehouse = warehouse;
  }

  private void log(String msg) {
    String t = LocalDateTime.now().format(fmt);
    System.out.println("[" + t + "][" + getName() + "] " + msg);
  }

  @Override
  public void run() {
    try (
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    ) {
      log("Connected from " + socket.getRemoteSocketAddress());
      // Send initial stock snapshot
      int[] init = warehouse.checkStock();
      out.println("STOCK " + init[0] + " " + init[1]);
      log("Sent initial STOCK " + init[0] + " " + init[1]);

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        inputLine = inputLine.trim();
        if (inputLine.isEmpty()) continue;
        log("Received: " + inputLine);

        String[] parts = inputLine.split("\\s+");
        String cmd = parts[0].toUpperCase();

        switch (cmd) {
          case "CHECK":
            int[] stock = warehouse.checkStock();
            out.println("STOCK " + "Apples:" + stock[0] + " " + "Oranges:" +  stock[1]);
            log("Replied STOCK " + "Apples" + stock[0] + " " + "Oranges" + stock[1]);
            break;

          case "BUY":
            if (parts.length != 3) {
              out.println("error wrong command");
              log("error, BUY command");
              break;
            }
            handleBuy(parts[1].toUpperCase(), parts[2], out);
            break;

          case "ADD":
            if (parts.length != 3) {
              out.println("ERROR bad command");
              log("Malformed ADD command");
              break;
            }
            handleAdd(parts[1].toUpperCase(), parts[2], out);
            break;

          case "QUIT":
            out.println("OK");
            log("Client requested QUIT");
            socket.close();
            return;

          default:
            out.println("error, unknown command");
            log("Unknown command: " + cmd);
        }
      }
    } catch (IOException e) {
      log("I/O error: " + e.getMessage());
    } finally {
      try { if (!socket.isClosed()) socket.close(); } catch (IOException ignored) {}
      log("Connection closed");
    }
  }

  private void handleBuy(String item, String numStr, PrintWriter out) {
    int n;
    try { n = Integer.parseInt(numStr); }
    catch (NumberFormatException e) {
      out.println("error, bad number");
      log("Bad number in BUY: " + numStr);
      return;
    }
    boolean ok;
    switch (item) {
      case "APPLES":
        ok = warehouse.buyApples(n);
        break;
      case "ORANGES":
        ok = warehouse.buyOranges(n);
        break;
      default:
        out.println(" error, unknown item");
        log("Unknown item in BUY: " + item);
        return;
    }
    if (ok) {
      out.println("OK");
      int[] s = warehouse.checkStock();
      log("BUY " + item + " " + n + " succeeded, new stock: " + s[0] + " apples, " + s[1] + " oranges");
    } else {
      out.println("error, insufficient stock");
      log("BUY " + item + " " + n + " failed (insufficient stock)");
    }
  }

  private void handleAdd(String item, String numStr, PrintWriter out) {
    int n;
    try { n = Integer.parseInt(numStr); }
    catch (NumberFormatException e) {
      out.println("error bad number");
      log("Bad number in ADD: " + numStr);
      return;
    }
    boolean ok;
    switch (item) {
      case "APPLES":
        ok = warehouse.addApples(n);
        break;
      case "ORANGES":
        ok = warehouse.addOranges(n);
        break;
      default:
        out.println("error unknown item");
        log("Unknown item in ADD: " + item);
        return;
    }
    if (ok) {
      out.println("OK");
      int[] s = warehouse.checkStock();
      log("ADD " + item + " " + n + " succeeded, new stock: " + s[0] + " apples, " + s[1] + " oranges");
    } else {
      out.println("FAIL bad_number_or_zero");
      log("ADD " + item + " " + n + " failed (bad number)");
    }
  }
}