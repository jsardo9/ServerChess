
import java.io.*; //for BufferedReader, InputStreamReader, PrintWriter
import java.net.*; //for ServerSocket, Socket

// Home IP Adress: 192.168.1.106
public class Server {

  private ServerConnection white;
  private ServerConnection black;

  public static void main(String[] args) throws IOException, InterruptedException {
    Server s = new Server();
  }

  public Server() throws IOException, InterruptedException {
    ServerSocket server = new ServerSocket(1910); // start server on port 1911

    /*
     * for (int i = 0; i < 100; i++) { Thread.sleep(300); String dots; if (i % 3 ==
     * 0) { dots = ".    \r"; } else if (i % 3 == 1) { dots = ". .   \r"; } else {
     * dots = ". . .\r"; } System.out.print(dots); }
     */

    System.out.println("Server now active");
    while (true) {
      System.out.println("Listening for client");
      Socket socket = server.accept(); // wait for client to connect
      System.out.println("Client has connected");

      if (white == null) {
        white = new ServerConnection(socket, this);
        white.send("White");
      } else if (white != null && black == null) {
        black = new ServerConnection(socket, this);
        black.send("Black");
      }
    }

  }

  public void transferMove(String move) {
    int color;
    color = Integer.parseInt(move.substring(4, 5));
    if (color == 0) {
      black.receiveMove(move.substring(0, 4));
      black.send("changeTurn");
      white.send("changeTurn");
    } else {
      white.receiveMove(move.substring(0, 4));
      black.send("changeTurn");
      white.send("changeTurn");
    }

  }

}
