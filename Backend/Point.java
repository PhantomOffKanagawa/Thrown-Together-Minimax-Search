package Backend;

public class Point
{
  private final byte x, y;

  // # Point is just x & y coordinates
  public Point(int x, int y) {
    this.x = (byte) x;
    this.y = (byte) y;
  }

  public int getX() { return((int) this.x); }
  public int getY() { return((int) this.y); }

  @Override
  public String toString() {
    return "Point [x=" + x + ", y=" + y + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Point other = (Point) obj;
    if (x != other.x)
      return false;
    if (y != other.y)
      return false;
    return true;
  }

  
}