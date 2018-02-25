public class maxarray {
    /** Returns the maximum value from m. */
    /*
    public static void max(int[] m) {
        int i = 0;
        int max = 0;
        while (i < m.length){
            if (m[i] > max)
                max = m[i];
            i = i + 1;
        }
        System.out.println(max);
    }
    */

    public static void max(int[] m){
        int max = 0;
        for (int i = 0; i < m.length; i += 1){
            if (m[i] > max)
                max = m[i];
        }
        System.out.println(max);
    }

    public static void main(String[] args) {
        int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};
        max(numbers);
    }
}