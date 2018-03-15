package disc;

public class Board {
    public static final int SIZE = 3; // Tic-Tac-Toe Boards are always 3x3
    private Piece[][] pieces;
    private boolean isXTurn;

    @Override
    public boolean equals(Object o) {
        Board O = (Board) o;
        return this.hashCode() == O.hashCode();
    }

    public int hashCode() {
        int hashcode = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++){
                hashcode += (i * SIZE + j) * pieces[i][j].hashCode();
            }
        return hashcode;
    }
}
