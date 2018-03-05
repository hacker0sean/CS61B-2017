package synthesizer;
import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T>{
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
        rb = (T[])new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isFull())
            throw new RuntimeException("Ring buffer overflow");
        else{
            fillCount += 1;
            rb[last] = x;
            last = (last + 1) % this.capacity;
        }
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        T result = rb[first];
        if(isEmpty())
            throw new RuntimeException("Ring buffer underflow");
        else{
            fillCount -= 1;
            rb[first] = null;
            first = (first + 1) % this.capacity;
        }
        return result;
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update 
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        return rb[first];
        // TODO: Return the first item. None of your instance variables should change.
    }

    public class ArrayWizard implements Iterator<T>{
        private int notionOfWhereHeIs;
        private int size;

        public ArrayWizard(){
            notionOfWhereHeIs = first;
            size = 0;
        }

        public boolean hasNext(){
            return (size < capacity);
        }

        public T next(){
            T currentThing = rb[notionOfWhereHeIs];
            notionOfWhereHeIs = (notionOfWhereHeIs + 1) % capacity;
            size += 1;
            return currentThing;
        }
    }

    @Override
    public Iterator<T> iterator(){
        return new ArrayWizard();
    }
    // TODO: When you get to part 5, implement the needed code to support iteration.
}
