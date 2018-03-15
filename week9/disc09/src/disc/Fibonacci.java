package disc;

import java.util.HashMap;
public class Fibonacci {
    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    // fib(0) = 0, fib(1) = 1, fib(2) = 1, fib(3) = 2, ...
    public Fibonacci(){
        map.put(0, 0);
        map.put(1, 1);
    }
    public int fib(int n) {
        if (map.containsKey(n))
            return n;
        else{
            int x = fib(n - 1) + fib(n - 2);
            map.put(n, x);
            return x;
        }
    }
}
