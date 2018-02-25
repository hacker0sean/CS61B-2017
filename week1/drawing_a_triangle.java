public class drawing_a_triangle{
    public static void drawTriangle(int n){
        int x = 1;
        while (x <= n){
            int j = 0;
            while (j < x){
                System.out.print("*");
                j = j + 1;
            }
            System.out.println();
            x = x + 1;
        }
    }

    public static void main(String[] avgs){
        drawTriangle(10);
    }
}