import edu.princeton.cs.algs4.Picture;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SeamCarver {
    public double[][] energys;
    public Map<Node, LinkedList<Integer>> Verical_node_to_min_route;
    public Map<Node, Double> Vertical_node_to_min_weight;
    public Map<Node, LinkedList<Integer>> Horizontal_node_to_min_route;
    public Map<Node, Double> Horizontal_node_to_min_weight;
    private Picture picture;
    private Node[][] nodes;


    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    public Picture picture()                       // current picture
    {
        return picture;
    }

    public int width()                         // width of current picture
    {
        return picture.width();
    }

    public int height()                        // height of current picture
    {
        return picture.height();
    }

    public double energy(int x, int y)            // energy of pixel at column x and row y
    {
        if (x < 0 || y < 0 || x >= width() || y >= height())
            throw new IndexOutOfBoundsException();
        int width = picture.width();
        int height = picture.height();
        int left = x - 1;
        if (left < 0)
            left = width - 1;
        int right = x + 1;
        if (right == width)
            right = 0;
        int up = y - 1;
        if (up < 0)
            up = height - 1;
        int down = y + 1;
        if (down == height)
            down = 0;
        double Rx = Math.abs(picture.get(left, y).getRed() - picture.get(right, y).getRed());
        double Gx = Math.abs(picture.get(left, y).getGreen() - picture.get(right, y).getGreen());
        double Bx = Math.abs(picture.get(left, y).getBlue() - picture.get(right, y).getBlue());
        double detx = Math.pow(Rx, 2) + Math.pow(Gx, 2) + Math.pow(Bx, 2);
        double Ry = Math.abs(picture.get(x, up).getRed() - picture.get(x, down).getRed());
        double Gy = Math.abs(picture.get(x, up).getGreen() - picture.get(x, down).getGreen());
        double By = Math.abs(picture.get(x, up).getBlue() - picture.get(x, down).getBlue());
        double dety = Math.pow(Ry, 2) + Math.pow(Gy, 2) + Math.pow(By, 2);
        return detx + dety;
    }

    public class Node {
        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    private HashMap<Node, LinkedList<Integer>> min_routePrep(){
        HashMap<Node, LinkedList<Integer>> min_route= new HashMap<>();
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                min_route.put(nodes[i][j], new LinkedList<>());
            }
        }
        return min_route;
    }

    public int[] findHorizontalSeam()            // sequence of indices for horizontal seam
    {
        findSeamPrep();
        Horizontal_node_to_min_route = min_routePrep();
        Horizontal_node_to_min_weight = new HashMap<>();
        for (int j = 0; j < height(); j++) {
            Horizontal_node_to_min_route.get(nodes[width() - 1][j]).add(j);
            Horizontal_node_to_min_weight.put(nodes[width() - 1][j], energys[width() - 1][j]);
        }

        for (int i = width() - 2; i > -1; i--) {
            for (int j = 0; j < height(); j++) {
                int interval_min;
                int interval_max;
                if (j != 0 && j != height() - 1) {
                    interval_min = j - 1;
                    interval_max = j + 2;
                } else if (j == 0) {
                    interval_min = j;
                    interval_max = j + 2;
                } else {
                    interval_min = j - 1;
                    interval_max = j + 1;
                }
                int min_choose = j - 1;
                double min_weight = Double.MAX_VALUE;
                for (int k = interval_min; k < interval_max; k++) {
                    if (Horizontal_node_to_min_weight.get(nodes[i + 1][k]) < min_weight) {
                        min_weight = Horizontal_node_to_min_weight.get(nodes[i + 1][k]);
                        min_choose = k;
                    }
                }

                Horizontal_node_to_min_route.get(nodes[i][j]).add(j);
                for (Integer w : Horizontal_node_to_min_route.get(nodes[i + 1][min_choose])) {
                    Horizontal_node_to_min_route.get(nodes[i][j]).add(w);
                }
                Horizontal_node_to_min_weight.put(nodes[i][j], energys[i][j] + min_weight);
            }
        }

        double weight = Double.MAX_VALUE;
        Node curr = null;
        for (Node i : Horizontal_node_to_min_weight.keySet()){
            if (i.x == 0) {
                if (Horizontal_node_to_min_weight.get(i) < weight) {
                    weight = Horizontal_node_to_min_weight.get(i);
                    curr = i;
                }
            }
        }
        LinkedList<Integer> x = Horizontal_node_to_min_route.get(curr);
        int[] result = new int[width()];
        int i = 0;
        for (Integer temp : x){
            result[i] = temp;
            i++;
        }
        return result;
    }

    private void findSeamPrep(){
        energys = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energys[i][j] = energy(i, j);
            }
        }
        nodes = new Node[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                nodes[i][j] = new Node(i, j);
            }
        }
    }

    public int[] findVerticalSeam()              // sequence of indices for vertical seam
    {
        findSeamPrep();
        Verical_node_to_min_route = min_routePrep();
        Vertical_node_to_min_weight = new HashMap<>();
        for (int i = 0; i < width(); i++) {
            Verical_node_to_min_route.get(nodes[i][height() - 1]).add(i);
            Vertical_node_to_min_weight.put(nodes[i][height() - 1], energys[i][height() - 1]);
        }

        for (int j = height() - 2; j > -1; j--) {
            for (int i = 0; i < width(); i++) {
                int interval_min;
                int interval_max;
                if (i != 0 && i != width()  - 1) {
                    interval_min = i - 1;
                    interval_max = i + 2;
                } else if (i == 0) {
                    interval_min = i;
                    interval_max = i + 2;
                } else {
                    interval_min = i - 1;
                    interval_max = i + 1;
                }
                int min_choose = i - 1;
                double min_weight = Double.MAX_VALUE;
                for (int k = interval_min; k < interval_max; k++) {
                    if (Vertical_node_to_min_weight.get(nodes[k][j + 1]) < min_weight) {
                        min_weight = Vertical_node_to_min_weight.get(nodes[k][j + 1]);
                        min_choose = k;
                    }
                }

                Verical_node_to_min_route.get(nodes[i][j]).add(i);
                for (Integer w : Verical_node_to_min_route.get(nodes[min_choose][j + 1])) {
                    Verical_node_to_min_route.get(nodes[i][j]).add(w);
                }
                Vertical_node_to_min_weight.put(nodes[i][j], energys[i][j] + min_weight);
            }
        }
        double weight = Double.MAX_VALUE;
        Node curr = null;
        for (Node i : Vertical_node_to_min_weight.keySet()){
            if (i.y == 0) {
                if (Vertical_node_to_min_weight.get(i) < weight) {
                    weight = Vertical_node_to_min_weight.get(i);
                    curr = i;
                }
            }
        }
        LinkedList<Integer> x = Verical_node_to_min_route.get(curr);
        int[] result = new int[height()];
        int i = 0;
        for (Integer temp : x){
            result[i] = temp;
            i++;
        }
        return result;
    }

    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from picture
    {
        if (seam.length != width())
            throw new IllegalArgumentException();
        for (int i = 0; i < width() - 1; i++){
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException();
        }
        SeamRemover.removeHorizontalSeam(picture, seam);
    }

    public void removeVerticalSeam(int[] seam)     // remove vertical seam from picture
    {
        if (seam.length != height())
            throw new IllegalArgumentException();
        for (int i = 0; i < height() - 1; i++){
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException();
        }
        SeamRemover.removeVerticalSeam(picture, seam);
    }
}
