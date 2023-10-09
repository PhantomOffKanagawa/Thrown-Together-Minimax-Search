package Backend;

import java.text.NumberFormat;
import java.util.LinkedList;

public class Minimax {

    // * Declare variables for keeping an active board, tracking execution time, tracking generated nodes, and tracking which player went last
    Board board;
    long time;
    long generatedNodes;
    int lastPlayer;

    public Minimax(Board board) {
        this.board = board;
    }

    // * As the first move is known... I hard coded it
    private void playerOneFirstMove() {
        board.setSquare(4, 3, 1);
        lastPlayer = 1;
        printMove();
        // printPage();
    }

    // * As the second move is known... I also hard coded it
    private void playerTwoFirstMove() {
        board.setSquare(3, 3, 2);
        lastPlayer = 2;
        printMove();
        // printPage();
    }

    // * Function to play out a game by switching between player choices until an end is reached
    public void playOutGame() {
        // * Make first two moves
        playerOneFirstMove();
        playerTwoFirstMove();

        // * Loop between 4 depth minimizing search and 2 depth maximizing search
        while (Math.abs(Minimax.heuristic(board)) != 1000 && !board.full()) {
            if (lastPlayer == 1) {
                minimax(2, 4);
            } else {
                minimax(1, 2);
            }

            printMove();
            // printPage();

        }
    }

    // * Function for generally managing a minimax search call (Can be collapsed into playOutGame())
    public void minimax(int player, int limit) {
        // * Reset Board and Minimax stats
        board.queuedMoves = new LinkedList<Point>();
        generatedNodes = 0;
        time = System.currentTimeMillis();

        // * Choose which recursive spiral to start on
        if (player == 1) {
            board = maximize(board, 0, limit);
        } else {
            board = minimize(board, 0, limit);
        }

        // * Upon completetion get the time and change the last player
        time = System.currentTimeMillis() - time;
        // * I am proud of my one liner solution for swapping between 1 and 2
        lastPlayer = (lastPlayer % 2) + 1;
    }

    // * Function for finding minimizing moves
    private Board minimize(Board board, int depth, int limit) {
        // * Add this generated node to total
        generatedNodes++;
        // * If at depth, terminate
        if (depth > limit)
            return board;
        depth++;

        // * If at terminal node, terminate
        if (Math.abs(heuristic(board)) == 1000)
            return board;

        // * Loop through all potential options and then return the board with the lowest heuristic value
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
                // Tie breaking handling
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

    // * Function for finding maximizing moves, See above, Technically can be collapsed into minimize
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

    // * Function to add up the total value of a board's heuristic
    public static int heuristic(Board board) {
        int score = 0;

        // * Array to represent the different direction sets by setting the xMult and yMult
        int[][] directions = { { 1, 0 }, { 0, 1 }, { 1, 1 }, { -1, 1 } };

        // * Loop through all four possible directions
        for (int dir[] : directions) {
            // * Track squares already checked for a direction
            LinkedList<Point> checkedSquares = new LinkedList<Point>();
            // * Loop over all squares set on the board
            for (Point square : board) {
                // * If square already accounted for, skip
                if (checkedSquares.contains(square))
                    continue;

                // * Return if it gets a win, else add the score ongoing
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

    // * Function to handle checking a line on a coordinate
    // * xMult and yMult are used to control direction
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
    // * Decides what score to give a line given the relevant information
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

    // * Function to print out a move and the relevant information about it
    public void printMove() {
        Point bestMove = board.getNextQueuedMove();
        System.out.printf(
                "Player %d placed piece at x:%d, y:%d, score: %d. This generated %s nodes and took %.3f seconds%n",
                lastPlayer, bestMove.getX(), bestMove.getY(), Minimax.heuristic(board),
                NumberFormat.getInstance().format(generatedNodes), ((float) time) / 1000);
    }

    // * Function to print out text representative of the moves made on a board
    public void printPage() {
        for (int y = 1; y <= board.getHeight(); y++) {
            for (int x = 1; x <= board.getLength(); x++) {
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
