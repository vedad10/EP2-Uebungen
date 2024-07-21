package AB3;

import AB2.Body;

/**
 * A list of bodies implemented as a singly linked list. The number of elements of the list is
 * not limited.
 */
//
// TODO: define further classes and methods for the implementation of the singly linked list, if
//  needed.
//

class MyListNode {
    private final Body body;
    private MyListNode next;

    MyListNode(Body body, MyListNode next) {
        this.body = body;
        this.next = next;
    }

    MyListNode(Body body) {
        this.body = body;
        this.next = null;
    }

    Body getBody() {
        return body;
    }

    Body get (int i){
        if (i == 0) return body;
        else return next.get(i - 1);
    }

    int indexOf (Body body, int i){
        if (body == this.body) return i;
        if(next == null) return -1;
        return next.indexOf(body, i + 1);
    }

    public MyListNode getNext() {
        return next;
    }

    public void setNext(MyListNode next) {
        this.next = next;
    }
    int size (){
        if (next == null) return 1;
        else return 1 + next.size();
    }
}

public class BodySinglyLinkedList {

    //TODO: declare variables.

    private MyListNode head;
    private MyListNode tail;

    /**
     * Initializes 'this' as an empty list.
     */
    public BodySinglyLinkedList() {

        // TODO: implement constructor.
        this.head = null;
        this.tail = null;
    }

    /**
     * Constructor: initializes this list as an independent copy of the specified list.
     * Calling methods of this list will not affect the specified list
     * and vice versa.
     *
     * @param list the list from which elements are copied to the new list, list != null.
     */
    public BodySinglyLinkedList(BodySinglyLinkedList list) {

        // TODO: implement constructor.
        this.head = list.head;
        this.tail = list.tail;
    }

    /**
     * Inserts the specified element 'b' at the beginning of this list.
     *
     * @param b the body that is added (b can also be 'null').
     */
    public void addFirst(Body b) {

        // TODO: implement method.
        head = new MyListNode(b, head);

    }

    /**
     * Appends the specified element 'b' to the end of this list.
     *
     * @param b the body that is added (b can also be 'null').
     */
    public void addLast(Body b) {

        // TODO: implement method.
        if (head == null) {
            head = tail = new MyListNode(b, null);
        } else tail.setNext(tail = new MyListNode(b, null));
    }

    /**
     * Returns the last element in this list (at the end of the list) without removing it.
     * Returns 'null' if the list is empty.
     */
    public Body getLast() {

        // TODO: implement method.
        if(head == null) return null;
        return tail.getBody();
    }

    /**
     * Returns the first element in this list (at the beginning of the list) without removing it.
     * Returns 'null' if the list is empty.
     */
    public Body getFirst() {

        // TODO: implement method.
        if(head == null) return null;
        return head.getBody();
    }

    /**
     * Retrieves and removes the first element in this list, that is, the element with index 0.
     * Indices of subsequent elements are decremented accordingly. Returns 'null' if the list is
     * empty.
     *
     * @return the first element in this list, or 'null' if the list is empty.
     */
    public Body pollFirst() {

        // TODO: implement method.
        if (head == null) return null;
        Body firstBody = head.getBody();
        head = head.getNext();
        return firstBody;
    }

    /**
     * Retrieves and removes the last element in this list, that is, the element with the highest
     * index. Returns 'null' if the list is empty.
     *
     * @return the last element in this list, or 'null' if the list is empty.
     */
    public Body pollLast() {

        // TODO: implement method.
        if(head == null) return null;
        if(head.getNext() == null) return null;

        Body lastBody = this.getLast();
        MyListNode new_last_body = head;

        while(new_last_body.getNext().getNext() != null){
            new_last_body = new_last_body.getNext();
        }
        new_last_body.setNext(null);
        return lastBody;
    }

    /**
     * Inserts the specified element at the specified position in this list, such that a
     * following invocation of get(i) would return 'b'. Shifts the element currently at that
     * position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param i the position of the new element in the list, i >= 0 && i <= size().
     * @param b the body that is added (b can also be 'null').
     */
    public void add(int i, Body b) {

        // TODO: implement method.
        if (i == 0) addFirst(b);
        else if (i == size() - 1) addLast(b);
        else {
            MyListNode element_to_add = new MyListNode(b);
            int index = i - 1;
            MyListNode current = head;
            for (int j = 0; j < size(); j++){
                if(j == index){
                    element_to_add.setNext(current.getNext());
                    current.setNext(element_to_add);
                }
                current = current.getNext();
            }
        }
    }

    /**
     * Returns the element with the specified index in this list. Returns 'null' if the list is
     * empty.
     *
     * @param i the list index of the element to be retrieved, i >= 0 && i < size().
     * @return the retrieved element at the specified position.
     */
    public Body get(int i) {

        // TODO: implement method.

        return head.get(i);
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list, or -1 if
     * this list does not contain the element. More formally, returns the lowest index i such
     * that b == get(i), or -1 if there is no such index.
     *
     * @param b the body to search for.
     * @return the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     */
    public int indexOf(Body b) {

        // TODO: implement method.
        if (head == null) return -1;
        return head.indexOf(b, 0);
    }

    /**
     * Returns the number of entries in this list (including 'null' entries).
     */
    public int size() {

        // TODO: implement method.
        if(head == null) return 0;
        return head.size();
    }

    public BodySinglyLinkedList keepFromTo(int fromIndex, int toIndex){

        if(head == null || fromIndex >= toIndex || fromIndex < 0 || toIndex >= size()) return null;

        MyListNode curr = head;

        for (int j = 0; j < fromIndex; j++){
            curr = curr.getNext();
        }
        head = curr;

        for (int j = fromIndex; j < toIndex; j++){
            curr = curr.getNext();
        }
        tail = curr;
        tail.setNext(null);

        return this;
    }
}