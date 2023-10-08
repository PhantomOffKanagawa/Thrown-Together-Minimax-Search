import java.util.Vector;

import Backend.Minimax;
import Backend.Board;

public class Main {
	public static void main(String[] args) {
        // * Initialize Board
        Board board = new Board();
        
        // Minimax.playerOneFirstMove(board);
        // Minimax.playerTwoFirstMove(board);

        board.setSquare(3, 2, 2);
        board.setSquare(4, 2, 1);
        board.setSquare(3, 3, 2);
        board.setSquare(4, 3, 2);
        board.setSquare(5, 3, 1);
        board.setSquare(2, 4, 2);
        board.setSquare(3, 4, 1);
        board.setSquare(4, 4, 1);
        board.setSquare(5, 4, 2);
        board.setSquare(3, 5, 1);

        System.out.printf("Current Heuristic Score is: %d", Minimax.heuristic(board));
    }
}