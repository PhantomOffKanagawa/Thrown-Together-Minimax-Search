package Backend;

import java.util.LinkedList;
import java.util.HashSet;
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

  private final byte[] boardArray;
  private final HashSet<Point> setSquares;
  protected final HashSet<Point> moveOptions;

  protected int score;
  protected LinkedList<Point> queuedMoves;

  public Board () {
    boardArray = new byte[HEIGHT * LENGTH];
    setSquares = new HashSet<Point>();
    moveOptions = new HashSet<Point>();
    score = 0;
    queuedMoves = new LinkedList<>();
  }

  public Board (Board board) {
    boardArray = board.boardArray.clone();
    setSquares = new HashSet<Point>(board.setSquares);
    moveOptions = new HashSet<Point>(board.moveOptions);
    queuedMoves = new LinkedList<>(board.queuedMoves);
  }

  protected void setSquare(int x, int y, int v) {
    boardArray[coordinatesToIndex(x, y)] = (byte) v;
    setSquares.add(new Point(x, y));
    for (int xN = Math.max(x - 1, 1); xN <= Math.min(x + 1, LENGTH); xN++)
        for (int yN = Math.max(y - 1, 1); yN <= Math.min(y + 1, HEIGHT); yN++)
          if (!setSquares.contains(new Point(xN, yN)))
            moveOptions.add(new Point(xN, yN));
          
    moveOptions.remove(new Point(x, y));
    queuedMoves.add(new Point(x, y));
  }

  public Point getNextQueuedMove() {
    return queuedMoves.removeFirst();
  }

  protected int getSquare(int x, int y) {
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