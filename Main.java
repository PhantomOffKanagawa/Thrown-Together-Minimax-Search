import java.util.Vector;

import Backend.Minimax;
import Backend.Board;

public class Main {
	public static void main(String[] args) {
        // * Initialize Board
        Board board = new Board();
        
        Minimax.playerOneFirstMove(board);
        Minimax.playerTwoFirstMove(board);
    }
}