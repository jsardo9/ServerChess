import java.io.*;
import java.net.*;

public class ServerConnection extends Thread
{ 
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private Server server;

  public ServerConnection(Socket sock, Server serv ) throws IOException
  {
    server = serv;
    socket = sock;
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
    
    start();
  }
  
  public void receiveMove(String move)
  {
    send(move);
  }
  
  
  public void sendMove(String guess)
  {
    send("move: " + guess);
  }
  
  public void send(String message)
  {
    System.out.println("text: " + message);
    out.println(message);
  }
  
  //override's Thread's run method
  public void run()
  {
    try
    {
      while (true)
      {
        String message = in.readLine();
        server.transferMove(message);
        
        
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}