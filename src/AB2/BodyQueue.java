package AB2;

/**
 * A queue of bodies. A collection designed for holding bodies prior to processing.
 * The bodies of the queue can be accessed in a FIFO (first-in-first-out) manner,
 * i.e., the body that was first inserted by 'add' is retrieved first by 'poll'.
 * The number of elements of the queue is not limited.
 */
public class BodyQueue {

    //TODO: declare variables.

    private int head; //head of the queue
    private int tail; //tail of the queue
    private int size; //capacity of the queue
    private int currentSize; //number of bodies in the queue
    private Body[] bodiesArray; //array of the bodies for queue implementation

    /**
     * Initializes this queue with an initial capacity.
     * @param initialCapacity the length of the internal array in the beginning,
     *                        initialCapacity > 0.
     */
    public BodyQueue(int initialCapacity) {

        //TODO: define constructor.
        this.bodiesArray = new Body[initialCapacity];
        this.size = initialCapacity;
        head = 0;
        tail = 0;
        currentSize = 0;
    }

    /**
     * Initializes this queue as an independent copy of the specified queue.
     * Calling methods of this queue will not affect the specified queue
     * and vice versa.
     * @param q the queue from which elements are copied to the new queue, q != null.
     */
    public BodyQueue(BodyQueue q) {

        //TODO: define constructor.
        this.bodiesArray = new Body[q.bodiesArray.length];

        System.arraycopy(q.bodiesArray, 0, this.bodiesArray, 0, bodiesArray.length);

        /* we can use this also for array copying:
        bodiesArray = q.bodiesArray.clone();

        or we can write our own function to copy the array :)
        */

        this.size = q.size;
        this.head = q.head;
        this.tail = q.tail;
        this.currentSize = q.currentSize;

    }

    /**
     * Adds the specified body 'b' to this queue.
     * @param b the element that is added to the queue.
     */
    public void add(Body b) {

        //TODO: implement method.

        if(isFull()){
            Body[] largerArray = new Body[bodiesArray.length + 1];
            System.arraycopy(bodiesArray, 0, largerArray, 0, bodiesArray.length);
            bodiesArray = largerArray;
        }
        bodiesArray[tail] = b;
        tail++;
        currentSize++;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns 'null' if this queue is empty.
     * @return the head of this queue (or 'null' if this queue is empty).
     */
    public Body poll() {

        //TODO: implement method.

        if(isEmpty()){
            return null;
        }
        Body DeletedElement = bodiesArray[head];
        for(int i = 0; i < tail - 1; i++){
            bodiesArray[i] = bodiesArray[i + 1];
        }
        if (tail < size)          //end of new queue looks like: ..... new tail, old tail and we need to remove old one
            bodiesArray[tail] = null;
            tail--;
            currentSize--;
            return DeletedElement;
    }

    /**
     * Returns the number of bodies in this queue.
     * @return the number of bodies in this queue.
     */
    public int size() {

        //TODO: implement method.
        return currentSize;
    }

    //My Methods:

    public boolean isFull(){
     return size == tail;
    }
    public boolean isEmpty() {
        return head == tail;
    }

    public int getLength(){
        return bodiesArray.length;
    }
    public Body[] getBodiesArray() {
        return bodiesArray;
    }
}
