package com.company;


public class exercise3{
    public static boolean findSum(int[] A, int x) {
        int a = 0;
        int b = A.length - 1;
        while (a <= b){
            if ((A[a] + A[b]) == x)
                return true;
            else if ((A[a] + A[b]) < x)
                a = a + 1;
            else
                b = b - 1;
        }
        return false;
    }
}