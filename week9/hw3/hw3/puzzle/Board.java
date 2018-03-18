package hw3.puzzle;
import edu.princeton.cs.algs4.Queue;

import java.util.Map;

public class Board implements WorldState{
    private int N;
    private final int[][] boards;
    private final static int BLANK = 0;
    public Board(int[][] tiles){
        N = tiles.length;
        boards = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++){
                boards[i][j] = tiles[i][j];
            }
    }

    public int tileAt(int i, int j){
        if (i < 0 || i >= N || j < 0 || j >= N)
            throw new IndexOutOfBoundsException();
        return boards[i][j];
    }

    public int size(){
        return N;
    }

    /** From : http://joshh.ug/neighbors.html
     * Returns neighbors of this board.
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int size = size();
        int iof0 = -1;
        int jof0 = -1;
        for (int rug = 0; rug < size; rug++) {
            for (int tug = 0; tug < size; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    iof0 = rug;
                    jof0 = tug;
                }
            }
        }
        int[][] temp = new int[size][size];
        for (int pug = 0; pug < size; pug++) {
            for (int yug = 0; yug < size; yug++) {
                temp[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Math.abs(-iof0 + i) + Math.abs(j - jof0) - 1 == 0) {
                    temp[iof0][jof0] = temp[i][j];
                    temp[i][j] = BLANK;
                    Board neighbor = new Board(temp);
                    neighbors.enqueue(neighbor);
                    temp[i][j] = temp[iof0][jof0];
                    temp[iof0][jof0] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming(){
        int count = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++){
                if (i == N - 1 && j == N - 1)
                    continue;
                if (this.boards[i][j] != (i * N + j + 1)){
                    count++;
                }
            }
        return count;
    }

    public int manhattan(){
        int count = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++){
                int number = boards[i][j];
                if (number == BLANK)
                    continue;
                int vertical = (number - 1) % N;
                int horizontal = (number - 1) / N;
                count += Math.abs(horizontal - i) + Math.abs(vertical - j);
            }
        return count;
    }

    public int estimatedDistanceToGoal(){
        return manhattan();
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Board board1 = (Board) o;

        if (this.size() != board1.size())
            return false;

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++){
                if (this.boards[i][j] != board1.boards[i][j])
                    return false;
            }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
