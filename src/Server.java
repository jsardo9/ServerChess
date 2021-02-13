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
    ServerSocket server = new ServerSocket(1910); // start server on port 191

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
