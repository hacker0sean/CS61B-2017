public class swap(){
    public static void swap(int x, int y) {
     int temp = y;
     y = x;
     x = temp;
     System.out.println(x); //12, 4
    }

    public static void swap(int[] x, int[] y) {
     int[] temp = y;
     y = x;
     x = temp;
     System.out.println(x[0]); //4
    }

    public static void main(String[] args) {
     int x1 = 42, y1 = 12;
     swap(x1, y1);
     System.out.println(x1); //42
     System.out.println(y1); //12
     int[] x2 = new int[] {1, 2, 3};
     int[] y2 = new int[] {4, 5, 6};
     swap(x2, y2);
     System.out.println(x2[0]); //1
     System.out.println(y2[0]); //4
     x2 = new int[] {1, 2, 3};
     y2 = new int[] {4, 5, 6};
     swap(x2[0], y2[0]);
     System.out.println(x2[0]); //1
     System.out.println(y2[0]); //4
    }
}
