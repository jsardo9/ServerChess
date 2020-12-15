import java.util.*;
import java.awt.event.*; //for ActionListener, ActionEvent
import javax.swing.*; //for JFrame, BoxLayout, JLabel, JTextField, JButton
import java.io.*;

public class PlayerGame {
  private GridDisplay display;
  private ClientConnection connection;
  private int color; // defaults 0 is white
  private boolean turn;

  // used when clicking peices
  private Location locClicked;
  private String imageClicked;
  private Color squareClicked;

  public PlayerGame() throws IOException {
    turn = true;
    color = 0; // 0 is white
    display = new GridDisplay(8, 8);
    display.setTitle("Not checkers... This Chess");

    locClicked = null;
    imageClicked = null;

    int alternater = 1;
    for (int j = 0; j < 8; j++) {
      for (int i = 0; i < 8; i++) {
        if (alternater % 2 == 1) {
          display.setColor(new Location(j, i), new Color(240, 217, 181));
        } else {
          display.setColor(new Location(j, i), new Color(181, 136, 99));
        }
        alternater++;
      }
      alternater++;
    }

    // white pieces

    display.setImage(new Location(7, 0), "Pieces/whiteRook.png");
    display.setImage(new Location(7, 1), "Pieces/whiteKnight.png");
    display.setImage(new Location(7, 2), "Pieces/whiteBishop.png");
    display.setImage(new Location(7, 3), "Pieces/whiteQueen.png");
    display.setImage(new Location(7, 4), "Pieces/whiteKing.png");
    display.setImage(new Location(7, 5), "Pieces/whiteBishop.png");
    display.setImage(new Location(7, 6), "Pieces/whiteKnight.png");
    display.setImage(new Location(7, 7), "Pieces/whiteRook.png");

    display.setImage(new Location(6, 0), "Pieces/whitePawn.png");
    display.setImage(new Location(6, 1), "Pieces/whitePawn.png");
    display.setImage(new Location(6, 2), "Pieces/whitePawn.png");
    display.setImage(new Location(6, 3), "Pieces/whitePawn.png");
    display.setImage(new Location(6, 4), "Pieces/whitePawn.png");
    display.setImage(new Location(6, 5), "Pieces/whitePawn.png");
    display.setImage(new Location(6, 6), "Pieces/whitePawn.png");
    display.setImage(new Location(6, 7), "Pieces/whitePawn.png");

    // black pieces
    display.setImage(new Location(0, 0), "Pieces/blackRook.png");
    display.setImage(new Location(0, 1), "Pieces/blackKnight.png");
    display.setImage(new Location(0, 2), "Pieces/blackBishop.png");
    display.setImage(new Location(0, 3), "Pieces/blackQueen.png");
    display.setImage(new Location(0, 4), "Pieces/blackKing.png");
    display.setImage(new Location(0, 5), "Pieces/blackBishop.png");
    display.setImage(new Location(0, 6), "Pieces/blackKnight.png");
    display.setImage(new Location(0, 7), "Pieces/blackRook.png");

    display.setImage(new Location(1, 0), "Pieces/blackPawn.png");
    display.setImage(new Location(1, 1), "Pieces/blackPawn.png");
    display.setImage(new Location(1, 2), "Pieces/blackPawn.png");
    display.setImage(new Location(1, 3), "Pieces/blackPawn.png");
    display.setImage(new Location(1, 4), "Pieces/blackPawn.png");
    display.setImage(new Location(1, 5), "Pieces/blackPawn.png");
    display.setImage(new Location(1, 6), "Pieces/blackPawn.png");
    display.setImage(new Location(1, 7), "Pieces/blackPawn.png");

    String ipAddress = JOptionPane.showInputDialog(display, "Enter Server IP Address");
    connection = new ClientConnection(ipAddress, display, this);

  }

  public void play() {
    while (true) {
      // wait 100 milliseconds for mouse or keyboard
      display.pause(100);

      // check if any locations clicked or keys pressed
      Location clickLoc = display.checkLastLocationClicked();
      int key = display.checkLastKeyPressed();

      if (clickLoc != null) {
        // a location was clicked
        locationClicked(clickLoc);
      }

      if (key != -1) {
        // a key was pressed
        keyPressed(key);
      }

    }
  }

  // called when the user clicks on a location.
  // that location is passed to this method.
  private void locationClicked(Location loc) {
    if (turn == true) {

      if (imageClicked == null) // means you havnt clicked an image yet
      {
        if (display.getImage(loc) != null) {
          if ((color == 0 && display.getImage(loc).substring(0, 5).equals("white"))
              || (color == 1 && display.getImage(loc).substring(0, 5).equals("black"))) // if cliocked your own peice
          {
            squareClicked = display.getColor(loc);
            display.setColor(loc, new Color(129, 150, 105)); // green
            imageClicked = display.getImage(loc);
            locClicked = loc;
          }
        }
      } else // second Click
      {
        // System.out.println(getLegalMoves(locClicked).isEmpty());

        if (display.getImage(loc) != null && ((color == 0 && display.getImage(loc).substring(0, 5).equals("white"))
            || (color == 1 && display.getImage(loc).substring(0, 5).equals("black")))) // if clicked own color
        {

          display.setColor(locClicked, squareClicked);
          squareClicked = display.getColor(loc);
          display.setColor(loc, new Color(129, 150, 105));
          imageClicked = display.getImage(loc);
          locClicked = loc;
        } else if (getLegalMoves(locClicked, color).contains(loc)) {

          display.setImage(locClicked, null);
          display.setColor(locClicked, squareClicked);
          display.setImage(loc, imageClicked);

          Location fromMove = new Location(7 - locClicked.getRow(), 7 - locClicked.getCol());
          Location toMove = new Location(7 - loc.getRow(), 7 - loc.getCol());

          connection.sendMove(fromMove, toMove, color);

          locClicked = null;
          imageClicked = null;
          squareClicked = null;
        }
      }
    }
  }

  // called when the user presses a key.
  // each key on the keyboard has a unique key code.
  // that key code is passed to this method.
  private void keyPressed(int key) {
    // print key code
    System.out.println("key code:  " + key);

    if (key == 32) {
      // space key was pressed

    } else {
      // some other key was pressed

      // show help message
      display.showMessageDialog("Press SPACE to move.\nClick to change color.");
    }
  }

  // this method is called at regular intervals
  public void step() {

  }

  public ArrayList<Location> getLegalMoves(Location location, int newColor) {
    String myColor;
    if (newColor == 0) {
      myColor = "white";
    } else {
      myColor = "black";
    }
    String piece = display.getImage(location);
    ArrayList<Location> legalMoves = new ArrayList<Location>();
    int row = location.getRow();
    int col = location.getCol();
    // System.out.println(piece.substring(5));
    if (piece.substring(5).equals("Pawn.png")) {

      if (row == 6) // accounts for pawns on first move
      {
        if (display.getImage(new Location(5, col)) == null) {
          legalMoves.add(new Location(5, col));

          if (display.getImage(new Location(4, col)) == null) {
            legalMoves.add(new Location(4, col));
          }
        }

      }

      if (col > 0 && row != 0) // checks left captures
      {

        if (display.getImage(new Location(row - 1, col - 1)) != null
            && !display.getImage(new Location(row - 1, col - 1)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row - 1, col - 1));
        }
      }

      if (col < 7 && row != 0) // checks right captures
      {
        if (display.getImage(new Location(row - 1, col + 1)) != null
            && !display.getImage(new Location(row - 1, col + 1)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row - 1, col + 1));
        }
      }

      if (display.getImage(new Location(row - 1, col)) == null) {
        legalMoves.add(new Location(row - 1, col));
      }

      return legalMoves;
    } else if (piece.substring(5).equals("Rook.png")) {
      for (int i = row - 1; i >= 0; i--) // does up moves
      {
        // System.out.println(i);
        if (display.getImage(new Location(i, col)) == null) {
          // System.out.println(new Location(i, col).toString());
          legalMoves.add(new Location(i, col));
        } else if (display.getImage(new Location(i, col)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, col)).substring(0, 5).equals(myColor)) {
          // System.out.println(new Location(i, col).toString());
          legalMoves.add(new Location(i, col));
          break;
        }
      }

      for (int i = row + 1; i < 8; i++) // does down moves
      {
        if (display.getImage(new Location(i, col)) == null) {
          legalMoves.add(new Location(i, col));
        } else if (display.getImage(new Location(i, col)).substring(0, 5).equals(myColor)) {
          // break;
        } else if (!display.getImage(new Location(i, col)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, col));
          break;
        }
      }

      for (int i = col - 1; i >= 0; i--) // does left moves
      {
        if (display.getImage(new Location(row, i)) == null) {
          legalMoves.add(new Location(row, i));
        } else if (display.getImage(new Location(row, i)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(row, i)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row, i));
          break;
        }
      }

      for (int i = col + 1; i < 8; i++) // does right moves
      {
        if (display.getImage(new Location(row, i)) == null) {
          legalMoves.add(new Location(row, i));
        } else if (display.getImage(new Location(row, i)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(row, i)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row, i));
          break;
        }
      }

      return legalMoves;
    }

    else if (piece.substring(5).equals("Bishop.png")) {

      for (int i = row + 1, j = col + 1; i < 8 && j < 8; i++, j++) // does down right moves
      {
        if (display.getImage(new Location(i, j)) == null) {
          legalMoves.add(new Location(i, j));
        } else if (display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, j));
          break;
        }
      }

      for (int i = row + 1, j = col - 1; i < 8 && j >= 0; i++, j--) // does down left moves
      {
        if (display.getImage(new Location(i, j)) == null) {
          legalMoves.add(new Location(i, j));
        } else if (display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, j));
          break;
        }
      }

      for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) // does up left moves
      {

        if (display.getImage(new Location(i, j)) == null) {

          legalMoves.add(new Location(i, j));
        } else if (display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          // break;
        } else if (!display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, j));
          break;
        }
      }

      for (int i = row - 1, j = col + 1; i >= 0 && j < 8; i--, j++) // does up right moves
      {
        if (display.getImage(new Location(i, j)) == null) {
          legalMoves.add(new Location(i, j));
        } else if (display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, j));
          break;
        }
      }

      return legalMoves;
    }

    else if (piece.substring(5).equals("Queen.png")) {
      for (int i = row + 1, j = col + 1; i < 8 && j < 8; i++, j++) // does down right moves
      {
        if (display.getImage(new Location(i, j)) == null) {
          legalMoves.add(new Location(i, j));
        } else if (display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, j));
          break;
        }
      }

      for (int i = row + 1, j = col - 1; i < 8 && j >= 0; i++, j--) // does down left moves
      {
        if (display.getImage(new Location(i, j)) == null) {
          legalMoves.add(new Location(i, j));
        } else if (display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, j));
          break;
        }
      }

      for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) // does up left moves
      {
        if (display.getImage(new Location(i, j)) == null) {
          legalMoves.add(new Location(i, j));
        } else if (display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, j));
          break;
        }
      }

      for (int i = row - 1, j = col + 1; i >= 0 && j < 8; i--, j++) // does up right moves
      {
        if (display.getImage(new Location(i, j)) == null) {
          legalMoves.add(new Location(i, j));
        } else if (display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, j)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, j));
          break;
        }
      }

      for (int i = row - 1; i >= 0; i--) // does up moves
      {
        if (display.getImage(new Location(i, col)) == null) {
          legalMoves.add(new Location(i, col));
        } else if (display.getImage(new Location(i, col)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, col)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, col));
          break;
        }
      }

      for (int i = row + 1; i < 8; i++) // does down moves
      {
        if (display.getImage(new Location(i, col)) == null) {
          legalMoves.add(new Location(i, col));
        } else if (display.getImage(new Location(i, col)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(i, col)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(i, col));
          break;
        }
      }

      for (int i = col - 1; i >= 0; i--) // does left moves
      {
        if (display.getImage(new Location(row, i)) == null) {
          legalMoves.add(new Location(row, i));
        } else if (display.getImage(new Location(row, i)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(row, i)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row, i));
          break;
        }
      }

      for (int i = col + 1; i < 8; i++) // does right moves
      {
        if (display.getImage(new Location(row, i)) == null) {
          legalMoves.add(new Location(row, i));
        } else if (display.getImage(new Location(row, i)).substring(0, 5).equals(myColor)) {
          break;
        } else if (!display.getImage(new Location(row, i)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row, i));
          break;
        }
      }
      return legalMoves;
    } else if (piece.substring(5).equals("King.png")) {
      if (display.isValid(new Location(row - 1, col - 1))) // 1
      {
        if (display.getImage(new Location(row - 1, col - 1)) == null) {
          legalMoves.add(new Location(row - 1, col - 1));
        } else if (!display.getImage(new Location(row - 1, col - 1)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row - 1, col - 1));
        }
      }

      if (display.isValid(new Location(row - 1, col))) // 2
      {
        if (display.getImage(new Location(row - 1, col)) == null) {
          legalMoves.add(new Location(row - 1, col));
        } else if (!display.getImage(new Location(row - 1, col)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row - 1, col));
        }
      }

      if (display.isValid(new Location(row - 1, col + 1))) // 3
      {
        if (display.getImage(new Location(row - 1, col + 1)) == null) {
          legalMoves.add(new Location(row - 1, col + 1));
        } else if (!display.getImage(new Location(row - 1, col + 1)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row - 1, col + 1));
        }
      }

      if (display.isValid(new Location(row, col - 1))) // 4
      {
        if (display.getImage(new Location(row, col - 1)) == null) {
          legalMoves.add(new Location(row, col - 1));
        } else if (!display.getImage(new Location(row, col - 1)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row, col - 1));
        }
      }

      if (display.isValid(new Location(row, col + 1))) // 5
      {
        if (display.getImage(new Location(row, col + 1)) == null) {
          legalMoves.add(new Location(row, col + 1));
        } else if (!display.getImage(new Location(row, col + 1)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row, col + 1));
        }
      }

      if (display.isValid(new Location(row + 1, col - 1))) // 6
      {
        if (display.getImage(new Location(row + 1, col - 1)) == null) {
          legalMoves.add(new Location(row + 1, col - 1));
        } else if (!display.getImage(new Location(row + 1, col - 1)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row + 1, col - 1));
        }
      }

      if (display.isValid(new Location(row + 1, col))) // 7
      {
        if (display.getImage(new Location(row + 1, col)) == null) {
          legalMoves.add(new Location(row + 1, col));
        } else if (!display.getImage(new Location(row + 1, col)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row + 1, col));
        }
      }

      if (display.isValid(new Location(row + 1, col + 1))) // 8
      {
        if (display.getImage(new Location(row + 1, col + 1)) == null) {
          legalMoves.add(new Location(row + 1, col + 1));
        } else if (!display.getImage(new Location(row + 1, col + 1)).substring(0, 5).equals(myColor)) {
          legalMoves.add(new Location(row + 1, col + 1));
        }
      }

      return legalMoves;
    } else if (piece.substring(5).equals("Knight.png")) {

      legalMoves.add(new Location(row - 2, col + 1));
      legalMoves.add(new Location(row - 2, col - 1));
      legalMoves.add(new Location(row - 1, col + 2));
      legalMoves.add(new Location(row - 1, col - 2));
      legalMoves.add(new Location(row + 1, col + 2));
      legalMoves.add(new Location(row + 1, col - 2));
      legalMoves.add(new Location(row + 2, col + 1));
      legalMoves.add(new Location(row + 2, col - 1));

      for (int i = 7; i >= 0; i--) {

        if ((display.isValid(legalMoves.get(i)) == false)) {

          legalMoves.remove(i);
        } else if (display.getImage(legalMoves.get(i)) == null) {

        } else if ((display.getImage(legalMoves.get(i)).substring(0, 5).equals(myColor))) {
          legalMoves.remove(i);

        }

      }
      return legalMoves;
    }

    System.out.println("havnt coded legalMoves for this peice yet");
    return legalMoves;

  }

  public void makeBlack() {
    color = 1;
    flipBoard();
    turn = false;
  }

  public void flipTurn() {
    System.out.println("try to promote");
    for (int i = 0; i < 8; i++) {
      System.out.println(i);
      if (display.isValid(new Location(0, i))) {
        // System.out.println((display.getImage(new Location(0,i))).substring(5));
      }

      if (display.getImage(new Location(0, i)) != null
          && (display.getImage(new Location(0, i))).substring(5).equals("Pawn.png")) {
        System.out.println("dodododododododo");
        if (color == 0) {
          display.setImage(new Location(0, i), null);
          display.setImage(new Location(0, i), "whiteQueen.png");
        } else {
          display.setImage(new Location(0, i), null);
          display.setImage(new Location(0, i), "blackQueen.png");
        }

      }

      if (display.getImage(new Location(7, i)) != null
          && (display.getImage(new Location(7, i))).substring(5).equals("Pawn.png")) {
        System.out.println("uhuuhuhuhuhhu");
        if (color == 1) {
          display.setImage(new Location(7, i), null);
          display.setImage(new Location(7, i), "whiteQueen.png");
        } else {
          display.setImage(new Location(7, i), null);
          display.setImage(new Location(7, i), "blackQueen.png");
        }

      }
    }

    if (turn) {
      turn = false;
    } else {
      turn = true;
    }
  }

  public void flipBoard() {
    display.setImage(new Location(7, 0), "Pieces/blackRook.png");
    display.setImage(new Location(7, 1), "Pieces/blackKnight.png");
    display.setImage(new Location(7, 2), "Pieces/blackBishop.png");
    display.setImage(new Location(7, 3), "Pieces/blackKing.png");
    display.setImage(new Location(7, 4), "Pieces/blackQueen.png");
    display.setImage(new Location(7, 5), "Pieces/blackBishop.png");
    display.setImage(new Location(7, 6), "Pieces/blackKnight.png");
    display.setImage(new Location(7, 7), "Pieces/blackRook.png");

    display.setImage(new Location(6, 0), "Pieces/blackPawn.png");
    display.setImage(new Location(6, 1), "Pieces/blackPawn.png");
    display.setImage(new Location(6, 2), "Pieces/blackPawn.png");
    display.setImage(new Location(6, 3), "Pieces/blackPawn.png");
    display.setImage(new Location(6, 4), "Pieces/blackPawn.png");
    display.setImage(new Location(6, 5), "Pieces/blackPawn.png");
    display.setImage(new Location(6, 6), "Pieces/blackPawn.png");
    display.setImage(new Location(6, 7), "Pieces/blackPawn.png");

    // white pieces
    display.setImage(new Location(0, 0), "Pieces/whiteRook.png");
    display.setImage(new Location(0, 1), "Pieces/whiteKnight.png");
    display.setImage(new Location(0, 2), "Pieces/whiteBishop.png");
    display.setImage(new Location(0, 3), "Pieces/whiteKing.png");
    display.setImage(new Location(0, 4), "Pieces/whiteQueen.png");
    display.setImage(new Location(0, 5), "Pieces/whiteBishop.png");
    display.setImage(new Location(0, 6), "Pieces/whiteKnight.png");
    display.setImage(new Location(0, 7), "Pieces/whiteRook.png");

    display.setImage(new Location(1, 0), "Pieces/whitePawn.png");
    display.setImage(new Location(1, 1), "Pieces/whitePawn.png");
    display.setImage(new Location(1, 2), "Pieces/whitePawn.png");
    display.setImage(new Location(1, 3), "Pieces/whitePawn.png");
    display.setImage(new Location(1, 4), "Pieces/whitePawn.png");
    display.setImage(new Location(1, 5), "Pieces/whitePawn.png");
    display.setImage(new Location(1, 6), "Pieces/whitePawn.png");
    display.setImage(new Location(1, 7), "Pieces/whitePawn.png");
  }

  // this code starts a game when you click the run button
  public static void main(String[] args) throws IOException {
    PlayerGame g = new PlayerGame();
    g.play();
  }
}
