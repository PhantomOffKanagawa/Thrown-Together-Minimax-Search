package Backend;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;

public class Board implements Iterable<Point>
{
  // * Constants for the board size
  private static final int HEIGHT = 5;
  private static final int LENGTH = 6;

  public static int getHeight() {
    return HEIGHT;
  }

  public static int getLength() {
    return LENGTH;
  }

  // * Initialize Variables to track: Board itself, All Squares that have been set and need to be checked, and all potential future options
  private final byte[] boardArray;
  private final HashSet<Point> setSquares;
  protected final HashSet<Point> moveOptions;

  // * Initialize Variables for the moves necessary to go to a generated board
  protected LinkedList<Point> queuedMoves;
  public int lastPlayer;

  // * Initialize a new board with blank sets
  public Board () {
    boardArray = new byte[HEIGHT * LENGTH];
    setSquares = new HashSet<Point>();
    moveOptions = new HashSet<Point>();
    queuedMoves = new LinkedList<>();
  }

  // * Initialize new boards as copies of the old board
  public Board (Board board) {
    boardArray = board.boardArray.clone();
    setSquares = new HashSet<Point>(board.setSquares);
    moveOptions = new HashSet<Point>(board.moveOptions);
    queuedMoves = new LinkedList<>(board.queuedMoves);
  }

  // * Sets the value of a square and related info
  protected void setSquare(int x, int y, int v) {
    // * Sets square value itself
    boardArray[coordinatesToIndex(x, y)] = (byte) v;
    // * Adds square to list of set values
    setSquares.add(new Point(x, y));
    // * Set all surrounding squares as options and remove the square that was chosen
    for (int xN = Math.max(x - 1, 1); xN <= Math.min(x + 1, LENGTH); xN++)
        for (int yN = Math.max(y - 1, 1); yN <= Math.min(y + 1, HEIGHT); yN++)
          if (!setSquares.contains(new Point(xN, yN)))
            moveOptions.add(new Point(xN, yN));
    moveOptions.remove(new Point(x, y));

    // * Add chosen square as a step to get to the board
    queuedMoves.add(new Point(x, y));
    lastPlayer = v;
  }

  // * Helper function to get the next move to be taken
  public Point getNextQueuedMove() {
    return queuedMoves.removeFirst();
  }

  // * Get the set value of a passed square
  protected int getSquare(int x, int y) {
    return boardArray[coordinatesToIndex(x, y)];
  }

  // * Convert 2 dimensional 1-based coordinates to 1 dimensional array index
  private int coordinatesToIndex(int x, int y) {
      return ((x - 1) * HEIGHT + (y - 1));
  }

  // Pass the iterator for the squares that need to be checked
  public Iterator<Point> iterator() {
    return setSquares.iterator();
  }

  // Check if all squares have been filled
  public boolean full() {
    return (setSquares.size() == HEIGHT * LENGTH);
  }
}