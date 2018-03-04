public class Spy<T> implements UnaryFunction<T>  {
    private Deque<T> history = new ArrayDeque<T>();
    private UnaryFunction<T> function;

    public Spy(UnaryFunction<T> f) {
        function = f;
    }

     @Override
     public T apply(T x) {
        history.addLast(x);
        return function.apply(x);
     }

     public void printArgumentHistory() {
        for(int i = 0; i < history.size(); i += 1){
            System.out.print(history.get(i) + " ");
        }
     }
}
