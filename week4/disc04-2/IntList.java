public class IntList {
    public int head;
    public IntList tail;

    public IntList(int i, IntList n) {
        head = i;
        tai l = n;
    }

    public void exercise2(IntList B){
        IntList A = this;
        while(A != null){
            IntList t1 = A.tail;
            A.tail = B.tail;
            B.tail = t1;
            B = A.tail;
            A = t1;
        }
    }
}
