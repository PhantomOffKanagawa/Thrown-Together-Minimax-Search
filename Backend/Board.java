package Backend;

import java.util.LinkedList;
import java.util.Iterator;

public class Board implements Iterable<Point>
{
  public static final int HEIGHT = 5;
  public static final int LENGTH = 6;

  public static int getHeight() {
    return HEIGHT;
  }

  public static int getLength() {
    return LENGTH;
  }

  private final byte[] boardArray = new byte[HEIGHT * LENGTH];
  private final LinkedList<Point> setSquares = new LinkedList<Point>();

  public void setSquare(int x, int y, int v) {
    boardArray[coordinatesToIndex(x, y)] = (byte) v;
    setSquares.add(new Point(x, y));
  }

  public int getSquare(int x, int y) {
    return boardArray[coordinatesToIndex(x, y)];
  }

  private int coordinatesToIndex(int x, int y) {
      return ((x - 1) * HEIGHT + (y - 1));
  }

  public Iterator<Point> iterator() {
    return setSquares.iterator();
  }

  public boolean full() {
    return (setSquares.size() == HEIGHT * LENGTH);
  }
}