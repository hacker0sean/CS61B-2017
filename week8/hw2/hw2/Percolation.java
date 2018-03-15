package hw2;

import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.ArrayList;

public class Percolation {
    private int size;
    private int numberOfOpenSite;
    private WeightedQuickUnionUF unionUF;
    private WeightedQuickUnionUF ConnectedTotup;
    private boolean[] open;
    private int virtualTop;
    private int virtualBottom;
    private int sites;

    private boolean valid(int row, int col){
        if(row < 0 || row >= size || col < 0 || col >= size)
            return false;
        return true;
    }

    public int xyTo1D(int row, int col){
        return row * size + col;
    }

    public int numberOfOpenSites()           // number of open sites
    {
        return numberOfOpenSite;
    }

    public boolean isOpen(int row, int col)  // is the site (row, col) open?
    {
        if(!(valid(row, col))){
            throw new IllegalArgumentException();
        }
        return open[xyTo1D(row, col)];
    }

    public Percolation(int N)                // create N-by-N grid, with all sites initially blocked
    {
        sites = N * N + 2;
        open = new boolean[N * N];
        unionUF = new WeightedQuickUnionUF(sites);
        ConnectedTotup = new WeightedQuickUnionUF(sites - 1);
        size = N;
        numberOfOpenSite = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                open[i * N + j] = false;
            }
        this.virtualTop = sites - 2;
        this.virtualBottom = sites - 1;
    }

    public void open(int row, int col)       // open the site (row, col) if it is not open already
    {
        if(!(valid(row, col))){
            throw new IllegalArgumentException();
        }
        int index = xyTo1D(row, col);
        if (open[index])
            return;
        open[index] = true;
        numberOfOpenSite ++;
        int indexup = -1;
        int indexdown = -1;
        int indexleft = -1;
        int indexright = -1;
        if (valid(row - 1, col))
            indexup = xyTo1D(row - 1, col);
        if (valid(row + 1, col))
            indexdown = xyTo1D(row + 1, col);
        if (valid(row, col - 1))
            indexleft = xyTo1D(row, col - 1);
        if (valid(row, col + 1))
            indexright = xyTo1D(row, col + 1);
        if (indexup != -1 && open[indexup]) {
            union(indexup, index);
        }
        if (indexdown != -1 && open[indexdown]){
            union(indexdown, index);
        }
        if (indexleft != -1 && open[indexleft]){
            union(indexleft, index);
        }
        if (indexright != -1 && open[indexright]){
            union(indexright, index);
        }
        if (row == 0){
            union(index, virtualTop);
        }
        if (row == size - 1){
            unionUF.union(index, virtualBottom);
        }
    }

    public void union(int index1, int index2){
        unionUF.union(index1, index2);
        ConnectedTotup.union(index1, index2);
    }

    public boolean percolates()              // does the system percolate?
    {
        return unionUF.connected(virtualBottom, virtualTop);
    }


    public boolean isFull(int row, int col)  // is the site (row, col) full?
    {
        if(!(valid(row, col))){
            throw new IllegalArgumentException();
        }
        int number = xyTo1D(row, col);
        return ConnectedTotup.connected(number, virtualTop);
    }



    public static void main(String[] args){
        Stopwatch watch = new Stopwatch();
        PercolationStats sta = new PercolationStats(100, 1000);
        System.out.println("elapsedTime : " + watch.elapsedTime() + "ms");
        System.out.println("mean :" + sta.mean());
        System.out.println("stddev : " + sta.stddev());
        System.out.println("Low : " + sta.confidenceLow());
        System.out.println("High : " + sta.confidenceHigh());
    }   // unit testing (not required)
}

