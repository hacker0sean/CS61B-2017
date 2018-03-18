package midterm;

import java.util.Map;
import java.util.HashSet;
import edu.princeton.cs.algs4.*;
import java.util.Set;
public class Main {
    private static final double b1 = 1;
    private static final double b2 = 3;
    public static double caclu(double a, double b){
        return (251.9 / (Math.pow(1000, b1) * (Math.pow(100, b2)))) * Math.pow(a, b1) * Math.pow(b, b2);
    }
    public static void main(String[] args) {
	    System.out.println(caclu(100, 10));
        System.out.println(caclu(500, 10));
        System.out.println(caclu(100, 50));
        System.out.println(caclu(500, 50));
        Graph G = new Graph(5);
        HashSet<Integer> blue = new HashSet<Integer>();
        HashSet<Integer> green = new HashSet<Integer>();
        twocolor(G, 0, blue, green);
        System.out.println("Blue vertices are: " + blue.toString());
    }


    public static void twocolor(Graph G, int v, Set<Integer> a, Set<Integer> b){
        a.add(v);
        for (int i : G.adj(v)) {
            if (a.contains(i)) {
                throw new IllegalArgumentException("graph is not bipartite"); }
            if (!b.contains(i)) {
                twocolor(G, i, b, a);
            }
        }
    }
}
