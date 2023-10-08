package Backend;

import java.util.LinkedList;
import java.util.Iterator;

public class Board implements Iterable<Point>
{
  private static final int length = 6;
  private final byte[] boardArray = new byte[30];
  private final LinkedList<Point> setSquares = new LinkedList<Point>();

  public void setSquare(int x, int y, int v) {
    boardArray[coordinatesToIndex(x, y)] = (byte) v;
    setSquares.add(new Point(x, y));
  }

  public int getSquare(int x, int y) {
    return (int) boardArray[coordinatesToIndex(x, y)];
  }

  private int coordinatesToIndex(int x, int y) {
      return ((x - 1) * length + (y - 1));
  }

  public Iterator<Point> iterator() {
    return setSquares.iterator();
  }
}