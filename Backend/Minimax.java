package Backend;

import java.util.Iterator;
import java.util.LinkedList;

public class Minimax {
    static final int LENGTH = 6;
    static final int HEIGHT = 5;

    Board board = new Board();

    public static void playerOneFirstMove(Board board) {
        board.setSquare(4, 3, 1);
        printOutMove(4, 3, 1);
    }

    public static void playerTwoFirstMove(Board board) {
        board.setSquare(3, 3, 2);
        printOutMove(3, 3, 2);
    }

    private static void printOutMove (int x, int y, int v) {
        System.out.printf("Player %s put a piece at x: %d, y: %d%n", (v == 1) ? "Player 1" : "Player 2", x, y);
    }

    private static int heuristic(Board board) {
        LinkedList<Point> checkedSquares = new LinkedList<Point>();
        int score = 0;

        for (Point square : board) {
            // * If square already accounted for, skip
            if (checkedSquares.contains(square)) continue;

            final int squarePlayer = board.getSquare(square.getX(), square.getY());

            // * Check horizontal
            score += checker(1, 0, board, square, squarePlayer, checkedSquares);
            // * Check vertical
            score += checker(0, 1, board, square, squarePlayer, checkedSquares);
            // * Check diagonal 1
            score += checker(1, 1, board, square, squarePlayer, checkedSquares);
            // * Check up diagonal 2
            score += checker(-1, 1, board, square, squarePlayer, checkedSquares);

        }

        return -1000;
    }

    private static int checker(int xMult, int yMult, Board board, Point square, int playerToCheckAgainst, LinkedList<Point> checkedSquares) {
        int numberInRow = 1, sidesOpen = 0;
        for (int d = -1; d < 2; d+=2) {
            for (int o = 1; o <= 3; o++) {
                int newX = square.getX() + xMult * (d * o);
                int newY = square.getY() + yMult * (d * o);
                
                // * Check if out of bounds
                if (newX == 0 || newX == LENGTH) break;
                if (newY == 0 || newY == HEIGHT) break;

                // * Status of square being checked
                int beingChecked = board.getSquare(newX, newY);
                // * If same as current square, add number of pieces in a row and don't check this square in the future
                if (beingChecked == playerToCheckAgainst) {
                    numberInRow++;
                    checkedSquares.add(new Point(newX, newY));
                // * If not currently any player, add one open side and end
                } else if (beingChecked == 0) {
                    sidesOpen++;
                    break;
                } else {
                // * If currently the other player, end
                    break;
                }
            }
        }

        return getScore(sidesOpen, numberInRow, playerToCheckAgainst);
    }

    // * Overly Long but hopefully fast Scoring function
    private static int getScore(int sidesOpen, int numberInRow, int whichPlayer) {
        if (numberInRow == 3) {
            if (sidesOpen == 2) {
                if (whichPlayer == 1) {
                    return 200;
                } else {
                    return -80;
                }
            } else {
                if (whichPlayer == 1) {
                    return 150;
                } else {
                    return -40;
                }
            }
        } else if (numberInRow == 2) {
            if (sidesOpen == 2) {
                if (whichPlayer == 1) {
                    return 20;
                } else {
                    return -15;
                }
            } else {
                if (whichPlayer == 1) {
                    return 5;
                } else {
                    return -2;
                }
            }
        } else if (numberInRow >= 4) {
            if (whichPlayer == 1) {
                return 1000;
            } else {
                return -1000;
            }
        }
        return 0;
    }


}
