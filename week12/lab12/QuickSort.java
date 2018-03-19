import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Quick;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(
            Queue<Item> unsorted, Item pivot,
            Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        while (!unsorted.isEmpty()){
            Item dequeueItem = unsorted.dequeue();
            int cmp = pivot.compareTo(dequeueItem);
            if (cmp < 0)
                greater.enqueue(dequeueItem);
            else if (cmp > 0)
                less.enqueue(dequeueItem);
            else
                equal.enqueue(dequeueItem);
        }
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(
            Queue<Item> items) {
        if (items.size() > 1) {
            Queue<Item> less = new Queue<>();
            Queue<Item> equal = new Queue<>();
            Queue<Item> greater = new Queue<>();
            Item pivot = items.peek();
            partition(items, pivot, less, equal, greater);
            items = quickSort(less);
            for (Item i : equal){
                items.enqueue(i);
            }
            for (Item i : quickSort(greater)){
                items.enqueue(i);
            }
        }
        return items;
    }

    public static void main(String[] args){
        Queue<Integer> queue = new Queue<>();
        for (int i = 0; i < 1000; i++){
            int k = (int)(Math.random() * 100);
            queue.enqueue(k);
        }
        System.out.println("Before Quicksort :" + queue);
        queue = QuickSort.quickSort(queue);
        System.out.print("After Quicksort : " + queue);
    }
}
