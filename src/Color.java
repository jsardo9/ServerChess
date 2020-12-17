public class Color
{
  private int red;
  private int green;
  private int blue;
  
  public Color(int r, int g, int b)
  {
    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255)
      throw new RuntimeException("Invalid color components:  " + r + ", " + g + ", " + b);
    red = r;
    green = g;
    blue = b;
  }
  
  public int getRed()
  {
    return red;
  }
  
  public int getGreen()
  {
    return green;
  }
  
  public int getBlue()
  {
    return blue;
  }
  
  public boolean equals(Color otherColor)
  {
    return red == otherColor.getRed() && green == otherColor.getGreen() && blue == otherColor.getBlue();
  }
  
  public String toString()
  {
    return "(" + red + ", " + green + ", " + blue + ")";
  }
}