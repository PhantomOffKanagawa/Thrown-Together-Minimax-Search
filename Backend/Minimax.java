package Backend;

public class Minimax {
    // final int length = 6;

    Board board = new Board();

    public static void playerOneFirstMove(Board board) {
        board.setSquare(4, 3, 1);
    }

    public static void playerTwoFirstMove(Board board) {
        board.setSquare(3, 3, 2);
    }


}
