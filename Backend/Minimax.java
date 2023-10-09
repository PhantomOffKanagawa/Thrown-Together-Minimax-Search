package Backend;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;

public class Minimax {

    Board board;
    long time;
    long generatedNodes;
    int lastPlayer;

    public Minimax(Board board) {
        this.board = board;
    }

    private void playerOneFirstMove() {
        board.setSquare(4, 3, 1);
        lastPlayer = 1;
        printMove();
        // printPage();
    }

    private void playerTwoFirstMove() {
        board.setSquare(3, 3, 2);
        lastPlayer = 2;
        printMove();
        // printPage();
    }

    public void playOutGame() {
        playerOneFirstMove();
        playerTwoFirstMove();

        while (Math.abs(Minimax.heuristic(board)) != 1000 && !board.full()) {
            if (lastPlayer == 1) {
                minimax(2, 0, 4);
            } else {
                minimax(1, 0, 2);
            }

            printMove();
            // printPage();

        }

    }

    public void minimax(int player, int depth, int limit) {
        board.queuedMoves = new LinkedList<Point>();
        generatedNodes = 0;
        time = System.currentTimeMillis();

        if (player == 1) {
            board = maximize(board, depth, limit);
        } else {
            board = minimize(board, depth, limit);
        }

        time = System.currentTimeMillis() - time;
        lastPlayer = (lastPlayer % 2) + 1;
    }

    private Board minimize(Board board, int depth, int limit) {
        generatedNodes++;
        if (depth > limit)
            return board;
        depth++;

        // * If at terminal node, terminate
        if (Math.abs(heuristic(board)) == 1000)
            return board;

        Board bestBoard = board;
        int bestScore = Integer.MAX_VALUE;
        for (Point square : board.moveOptions) {
            Board newBoard = new Board(board);
            newBoard.setSquare(square.getX(), square.getY(), 2);
            int newScore = heuristic(maximize(newBoard, depth, limit));

            if (newScore < bestScore) {
                bestBoard = newBoard;
                bestScore = newScore;
            } else if (newScore == bestScore) {
                if (newBoard.queuedMoves.getLast().getX() < bestBoard.queuedMoves.getLast().getX()
                        || (newBoard.queuedMoves.getLast().getX() == bestBoard.queuedMoves.getLast().getX()
                                && newBoard.queuedMoves.getLast().getY() < bestBoard.queuedMoves.getLast().getY())) {
                    bestBoard = newBoard;
                    bestScore = newScore;
                }
            }
        }

        return bestBoard;
    }

    private Board maximize(Board board, int depth, int limit) {
        generatedNodes++;
        if (depth > limit)
            return board;
        depth++;

        // * If at terminal node, terminate
        if (Math.abs(heuristic(board)) == 1000)
            return board;

        Board bestBoard = board;
        int bestScore = Integer.MIN_VALUE;
        for (Point square : board.moveOptions) {
            Board newBoard = new Board(board);
            newBoard.setSquare(square.getX(), square.getY(), 1);
            int newScore = heuristic(minimize(newBoard, depth, limit));

            if (newScore == 1000)
                return newBoard;

            if (newScore > bestScore) {
                bestBoard = newBoard;
                bestScore = newScore;
            } else if (newScore == bestScore) {
                if (newBoard.queuedMoves.getLast().getX() < bestBoard.queuedMoves.getLast().getX()
                        || (newBoard.queuedMoves.getLast().getX() == bestBoard.queuedMoves.getLast().getX()
                                && newBoard.queuedMoves.getLast().getY() < bestBoard.queuedMoves.getLast().getY())) {
                    bestBoard = newBoard;
                    bestScore = newScore;
                }
            }
        }

        return bestBoard;
    }

    public static int heuristic(Board board) {
        int score = 0;

        int[][] directions = { { 1, 0 }, { 0, 1 }, { 1, 1 }, { -1, 1 } };

        for (int dir[] : directions) {
            LinkedList<Point> checkedSquares = new LinkedList<Point>();
            for (Point square : board) {
                // * If square already accounted for, skip
                if (checkedSquares.contains(square))
                    continue;

                final int squarePlayer = board.getSquare(square.getX(), square.getY());
                int scoreInc = checker(dir[0], dir[1], board, square, squarePlayer, checkedSquares);
                if (Math.abs(scoreInc) == 1000)
                    return scoreInc;
                score += scoreInc;
            }
        }

        // * If tie return 0
        if (board.full())
            return 0;

        return score;
    }

    private static int checker(int xMult, int yMult, Board board, Point square, int playerToCheckAgainst,
            LinkedList<Point> checkedSquares) {
        int numberInRow = 1, sidesOpen = 0;
        for (int d = -1; d < 2; d += 2) {
            for (int o = 1; o <= 3; o++) {
                int newX = square.getX() + xMult * (d * o);
                int newY = square.getY() + yMult * (d * o);

                // * Check if out of bounds
                if (newX <= 0 || newX >= board.getLength() + 1)
                    break;
                if (newY <= 0 || newY >= board.getHeight() + 1)
                    break;

                // * Status of square being checked
                int beingChecked = board.getSquare(newX, newY);
                // * If same as current square, add number of pieces in a row and don't check
                // this square in the future
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
            } else if (sidesOpen == 1) {
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
            } else if (sidesOpen == 1) {
                if (whichPlayer == 1) {
                    return 5;
                } else {
                    return -2;
                }
            }
        } else if (numberInRow >= 4) {
            if (whichPlayer == 1) {
                // System.out.println("Victory for p1 Found");
                return 1000;
            } else {
                // System.out.println("Victory for p2 Found");
                return -1000;
            }
        }
        return 0;
    }

    public void printMove() {
        Point bestMove = board.getNextQueuedMove();
        System.out.printf(
                "Player %d placed piece at x:%d, y:%d, score: %d. This generated %s nodes and took %.3f seconds%n",
                lastPlayer, bestMove.getX(), bestMove.getY(), Minimax.heuristic(board),
                NumberFormat.getInstance().format(generatedNodes), ((float) time) / 1000);
    }

    public void printPage() {
        for (int y = 1; y <= board.HEIGHT; y++) {
            for (int x = 1; x <= board.LENGTH; x++) {
                System.out.printf("|");
                char symbol = ' ';
                if (board.getSquare(x, y) == 1)
                    symbol = 'X';
                else if (board.getSquare(x, y) == 2)
                    symbol = 'O';
                System.out.printf(" %c |", symbol);
            }
            System.out.println();
        }
    }

}
