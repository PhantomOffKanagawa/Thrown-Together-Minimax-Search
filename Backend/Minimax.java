package Backend;

import java.util.Iterator;
import java.util.LinkedList;

public class Minimax {
    final int length = 6;
    final int height = 5;

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

            // * Check for across


            score += getScore(0, 0, 0);
        }

        return -1000;
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
