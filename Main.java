import java.util.Vector;

import Backend.Minimax;
import Backend.Point;
import Backend.Board;

public class Main {
	public static void main(String[] args) {
        // * Initialize Board
        Board board = new Board();
        // * Initialize minimax runner
        Minimax mm = new Minimax(board);

        // * Run the minimax strategy
        mm.playOutGame();
    }
}