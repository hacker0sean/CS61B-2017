public class ArrayDeque<T> {
    private int size;
    private T[] item;
    private int head;
    private int tail;
    private double ratio = 0.25;

    private void resize(int resize_size){
        T[] NewArray = (T[]) new Object[resize_size];
        int OldLength = item.length;
        int FirstLength = OldLength - head - 1;
        System.arraycopy(item, head + 1, NewArray,0, FirstLength);
        System.arraycopy(item, 0, NewArray, FirstLength, OldLength - FirstLength);
        head = resize_size - 1;
        tail = OldLength - 1;
        item = NewArray;
    }

    private void remove_resize(int resize_size){
        if (head < tail){
            T[] NewArray = (T[]) new Object[resize_size];
            System.arraycopy(item, head + 1, NewArray, 0, size);
            item = NewArray;
            head = resize_size - 1;
            tail = size - 1;
        }
        else
            resize(resize_size);
    }

    public ArrayDeque(){
        size = 0;
        item = (T[]) new Object[8];
        head = 0;
        tail = 0;
    }

    public void addFirst(T x){
        if (size == item.length){
            resize(item.length * 2);
        }
        item[head] = x;
        if (head == 0)
            head = item.length - 1;
        else
            head = head - 1;
        size += 1;
    }

    public void addLast(T x){
        if (size == item.length){
            resize(item.length * 2);
        }
        item[tail + 1] = x;
        if (tail == item.length - 1)
            tail = 0;
        else
            tail = tail + 1;
        size += 1;
    }

    public boolean isEmpty(){
        if (size == 0)
            return true;
        return false;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        int temp;
        if (head == item.length - 1)
            temp = 0;
        else
            temp = head + 1;
        for(int i = 0; i < size; i += 1){
            System.out.print(item[temp] + " ");
            temp = (temp + 1) % item.length;
        }
    }

    public T removeFirst(){
        size -= 1;
        head = (head + 1) % item.length;
        T result = item[head];
        item[head] = null;
        if ((size < item.length * ratio) && (item.length >= 16)){
            remove_resize((int)item.length / 2);
        }
        return result;
    }

    public T removeLast(){
        size -= 1;
        T result = item[tail];
        item[tail] = null;
        if (tail == 0)
            tail = item.length - 1;
        else
            tail = tail - 1;
        if ((size < item.length * ratio) && (item.length >= 16)){
            remove_resize((int)item.length / 2);
        }
        return result;
    }

    public T get(int index){
        return item[(head + 1 + index) % item.length];
    }

    public static void main(String[] args){
        System.out.println("Running add/isEmpty/Size test.");
        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for(int i = 0; i < 16; i = i + 2){
            lld1.addFirst(i);
            lld1.addLast(i + 1);
        }
        lld1.printDeque();
        for(int i = 0; i < 16; i = i + 2){
            lld1.removeFirst();
            lld1.removeLast();
        }
        System.out.println("");
        System.out.println(lld1.get(5));
        System.out.println("Printing out deque: ");
    }

}
