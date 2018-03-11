package com.company;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.*;

public class exercise4 {
    public static int[] union(int[] A, int[] B) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int x : A){
            if (map.containsKey(x)){
                map.put(x, map.get(x) + 1);
            }
            else{
                map.put(x,0);
            }
        }

        for (int x : B){
            if (map.containsKey(x)){
                map.put(x, map.get(x) + 1);
            }
            else{
                map.put(x,0);
            }
        }

        int[] result = new int[map.keySet().size()];
        int i = 0;
        for (Integer x : map.keySet()){
            result[i] = x;
            i++;
        }
        return result;
    }

    public static int[] intersection(int[] A, int[] B) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int x : A){
            if (map.containsKey(x)){
                map.put(x, map.get(x) + 1);
            }
            else{
                map.put(x,0);
            }
        }

        for (int x : B){
            if (map.containsKey(x)){
                map.put(x, map.get(x) + 1);
            }
            else{
                map.put(x,0);
            }
        }
        Set<Integer> interset = new HashSet<Integer>();
        for (Integer x : map.keySet()){
            if (map.get(x) > 0)
                interset.add(x);
        }
        int[] result = new int[interset.size()];
        int i = 0;
        for (Integer x : interset){
            result[i] = x;
            i += 1;
        }
        return result;
    }

    @Test
    public void testunion(){
        int A[] = {1, 2, 3, 4};
        int B[] = {3, 4, 5, 6};
        int expected[] = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected, union(A, B));
    }

    @Test
    public void testintersection(){
        int A[] = {1, 2, 3, 4};
        int B[] = {3, 4, 5, 6};
        int expected[] = {3, 4};
        assertArrayEquals(expected, intersection(A, B));
    }
}