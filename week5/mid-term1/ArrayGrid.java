public class ArrayGrid implements Grid {
    private int[][] nums;
    private int rows; /* Number of rows in the Grid. */
    private int cols; /* Number of cols in the Grid. */

    /** Creates a number grid initially full of zeroes, where r and c > 0.
     * nums is created with extra space to avoid immediate resizing. */
    public ArrayGrid(int r, int c) {
        nums = new int[r * 2][c * 2];
        rows = r; cols = c;
    }
    /** Resizes the underlying nums array to be r arrays of length c each. */
    private void resize(int r, int c) { /* Code not shown */ }
    /** Gets a copy of the cth column. */
    private int[] getColCopy(int c) { /* Code not shown */ }
    @Override
    /** Prints out the grid. */
    public void print() { /* Code not shown */ }

    @Override
    public void addRow(int[] row) {
        if (rows >= nums.length) {
            resize(nums.length * 2, nums[0].length);
        }
        System.arraycopy(row, 0, nums[rows], 0, cols);
        rows++;
    }

    public void rotateColumn (int c, int x) {
        int[] copy = getColCopy(c);
        for (int i = 0; i < rows; i++) {
            nums[(i + x) % rows][c] = copy[i];
        }  
    }
}