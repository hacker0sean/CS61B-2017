package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    double[] sta;
    public PercolationStats(int N, int T)   // perform T independent experiments on an N-by-N grid
    {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException();
        sta = new double[T];
        for (int i = 0; i < T; i++){
            Percolation per = new Percolation(N);
            while (!(per.percolates())){
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                per.open(row, col);
            }
            sta[i] = per.numberOfOpenSites() * 1.0 / (N * N);
        }
    }

    public double mean()                    // sample mean of percolation threshold
    {
        return StdStats.mean(sta);
    }

    public double stddev()                  // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(sta);
    }

    public double confidenceLow()           // low  endpoint of 95% confidence interval
    {
        return mean() - 1.96 * Math.sqrt(stddev()) / (Math.sqrt(sta.length));
    }

    public double confidenceHigh()          // high endpoint of 95% confidence interval
    {
        return mean() + 1.96 * Math.sqrt(stddev()) / (Math.sqrt(sta.length));
    }
}
