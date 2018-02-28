public class LinkedListDeque<T>{
    private Node sentinel;
    private int size;

    private class Node{
        public Node prev;
        public T item;
        public Node next;

        public Node(Node prev, T item, Node next){
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public LinkedListDeque(){
        size = 0;
        sentinel = new Node(null, null,null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public LinkedListDeque(T x){
        size = 1;
        sentinel = new Node(null,null,null);
        sentinel.next = new Node(sentinel, x, sentinel);
        sentinel.prev = sentinel.next;
    }

    public void addFirst(T Item){
        size += 1;
        Node temp = sentinel.next;
        sentinel.next = new Node(sentinel, Item, temp);
        temp.prev = sentinel.next;
    }

    public void addLast(T Item){
        size += 1;
        Node temp = sentinel.prev;
        sentinel.prev = new Node(temp, Item, sentinel);
        temp.next = sentinel.prev;
    }

    public boolean isEmpty(){
        if (size == 0){
            return true;
        }
        return false;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        Node temp = sentinel.next;
        for (int i = 0; i < size; i++){
            System.out.print(temp.item + " ");
            temp = temp.next;
        }
    }

    public T removeFirst(){
        if (size == 0) {
            return null;
        }
        size -= 1;
        T result = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return result;
    }

    public T removeLast(){
        if (size == 0){
            return null;
        }
        size -= 1;
        T result = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return result;
    }

    public T get(int index){
        Node result = sentinel.next;
        for (int i = 0; i < index; i++){
            result = result.next;
        }
        return result.item;
    }

    private T NodeGetRecursive(int index, Node cur) {
        if (index == 0){
            return cur.item;
        }
        return NodeGetRecursive(index - 1, cur.next);
    }

    public T getRecursive(int index){
        return NodeGetRecursive(index, sentinel.next);
    }
}