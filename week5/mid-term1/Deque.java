public interface Deque<Item> {
    void addFirst(Item x); void addLast(Item x);
    boolean isEmpty(); int size();
    void printDeque(); Item get(int index);
    Item removeFirst(); Item removeLast();
    default boolean remove(Item x) {
        int length = this.size();
        boolean remove = false;
        for (int i = 0; i < length; i++){
            Item s = removeFirst();
            if (s != null && s.equals(x))
                remove = true;
            else
                this.addLast(s);
        }
        return remove;
    }
}