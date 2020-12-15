import java.io.*;  //for BufferedReader, InputStreamReader, PrintWriter
import java.net.*;  //for Socket

public class ClientConnection extends Thread
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private GridDisplay display;
  private PlayerGame game;

  public ClientConnection(String ipAddress, GridDisplay display, PlayerGame g) throws IOException
  {
    this.display = display;
    socket = new Socket(ipAddress, 1910);
    game = g;
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);

    start();
  }

  public void sendMove(Location initial, Location fnal, int color)
  {
    send("" + initial.getRow() + initial.getCol() + fnal.getRow() + fnal.getCol() + color);
  }

  private void send(String message)
  {
    System.out.println("sending:  " + message);
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
        // messages like "5254"
        System.out.println("received:  " + message);

        if (message.equals("Black"))
        {
          game.makeBlack();
        }
        else if (message.equals("White"))
        {

        }
        else if (message.equals("changeTurn"))
        {
          game.flipTurn();
        }
          else
        {
         System.out.println("made it");
        Location initial = new Location (Integer.parseInt(message.substring(0,1)) , Integer.parseInt(message.substring(1,2)));
        Location fnal = new Location (Integer.parseInt(message.substring(2,3)) , Integer.parseInt(message.substring(3,4)));
        String peice = display.getImage(initial);
        display.setImage(initial, null);
        display.setImage(fnal, peice);
        }




      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
