package disc;

public class Piece {
    private String type; // Will be either "X" or "O".

    public boolean equals(Object o) {
        Piece otherPiece = (Piece) o;
        return this.type.equals(otherPiece.type);
    }

    public int hashCode() {
        if (type.equals("X"))
            return 2;
        else if (type.equals("O"))
            return 1;
        return 0;
    }
}
