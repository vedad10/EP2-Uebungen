package AB4;

/**
 * A list of 'IntVar' objects implemented as a doubly linked list. The number of elements of the
 * list is not limited. The list may also contain entries of 'null'.
 */
//
// TODO: define further classes and methods for the implementation of the doubly linked list, if
//  needed.
//

class MyListNode {
    private MyListNode prev, next;
    private  IntVar body;

    MyListNode(IntVar body, MyListNode prev, MyListNode next) {
        this.body = body;
        this.prev = prev;
        this.next = next;
    }

    MyListNode(IntVar body) {
        this.body = body;
        this.next = this.prev = null;
    }

    public IntVar getBody() {
        return body;
    }
    public void setBody(IntVar body) {
        this.body = body;
    }


    public MyListNode getNext() {
        return next;
    }

    public void setNext(MyListNode next) {
        this.next = next;
        if (next != null) {
            next.prev = this;
        }
    }

    public MyListNode getPrev() {
        return prev;
    }

    public void setPrev(MyListNode prev) {
        this.prev = prev;
        if (prev != null) {
            prev.next = this;
        }
    }
}

public class IntVarDoublyLinkedList {

    //TODO: declare variables.
    private MyListNode head;
    private MyListNode tail;

    /**
     * Initializes 'this' as an empty list.
     */
    public IntVarDoublyLinkedList() {

        //TODO: implement constructor.
        this.head = null;
        this.tail = null;
    }

    /**
     * Inserts the specified element 'v' at the beginning of this list.
     *
     * @param v the variable that is added ('v' can also be 'null').
     */
    public void addFirst(IntVar v) {

        //TODO: implement method.
        if (v == null) return;
        MyListNode newNode = new MyListNode(v, null, head);
        if (head != null) {
            head.setPrev(newNode);
        }
        head = newNode;
        if (tail == null) {
            tail = newNode;
        }
    }

    /**
     * Appends the specified element 'v' to the end of this list.
     *
     * @param v the variable that is added ('v' can also be 'null').
     */
    public void addLast(IntVar v) {

        //TODO: implement method.
        if (v == null) return;
        MyListNode newNode = new MyListNode(v, tail, null);
        if (tail != null) {
            tail.setNext(newNode);
        }
        tail = newNode;
        if (head == null) {
            head = newNode;
        }
    }


    /**
     * Returns the last element in this list (at the end of the list) without removing it.
     * Returns 'null' if the list is empty.
     */
    public IntVar getLast() {

        //TODO: implement method.
        if (head == null || size() == 0) {
            return null;
        }
        return tail.getBody();
    }

    /**
     * Returns the first element in this list (at the beginning of the list) without removing it.
     * Returns 'null' if the list is empty.
     */
    public IntVar getFirst() {

        //TODO: implement method.
        if(head == null){
            return null;
        }
        else return head.getBody();
    }

    /**
     * Retrieves and removes the first element in this list, that is, the element with index 0.
     * Indices of subsequent elements are decremented accordingly. Returns 'null' if the list is
     * empty.
     *
     * @return the first element in this list, or 'null' if the list is empty.
     */
    public IntVar pollFirst() {

        //TODO: implement method.
        if (head == null) {
            return null;
        }
        IntVar result = head.getBody();
        head = head.getNext();
        if (head != null) {
            head.setPrev(null);
        } else {
            tail = null;
        }
        return result;
    }

    /**
     * Retrieves and removes the last element in this list, that is, the element with the highest
     * index. Returns 'null' if the list is empty.
     *
     * @return the last element in this list, or 'null' if the list is empty.
     */
    public IntVar pollLast() {

        //TODO: implement method.
        if (tail == null) return null;
        IntVar result = tail.getBody();
        tail = tail.getPrev();
        if (tail != null) {
            tail.setNext(null);
        } else {
            head = null;
        }
        return result;
    }

    /**
     * Inserts the specified element at the specified position in this list, such that a
     * following invocation of get(i) would return 'v'. Shifts the element currently at that
     * position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param i the position of the new element in the list, i >= 0 && i <= size().
     * @param v the body that is added ('v' can also be 'null').
     */
    public void add(int i, IntVar v) {

        //TODO: implement method.
            if (v == null || i < 0 || i > size()) return;

            if (i == 0) {
                addFirst(v);
                return;
            } else if (i == size()) {
                addLast(v);
                return;
            }

            MyListNode current = head;
            for (int index = 1; index < i; index++) {
                current = current.getNext();
            }

            MyListNode newNode = new MyListNode(v, current, current.getNext());
            current.setNext(newNode);
            if (newNode.getNext() != null) {
                newNode.getNext().setPrev(newNode);
            }
        }

    /**
     * Returns the element with the specified index in this list. Returns 'null' if the list is
     * empty.
     *
     * @param i the list index of the element to be retrieved, i >= 0 && i < size().
     * @return the retrieved element at the specified position.
     */

    public IntVar get(int i) {

        //TODO: implement method.
        if (head == null || i < 0 || i >= size()) return null;
        MyListNode node = head;
        for (int index = 0; index < i; index++) {
            node = node.getNext();
        }
        return node.getBody();
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param i the index of the element to be replaced, i >= 0 && i < size().
     * @param v the new element to be set at the specified index ('v' can also be 'null').
     * @return the element that was replaced.
     */
    public IntVar set(int i, IntVar v) {

        //TODO: implement method.
        if (i < 0 || i >= size() || head == null) {
            return null;
        }
        MyListNode current = head;
        for (int index = 0; index < i; index++) {
            current = current.getNext();
        }
        IntVar oldBody = current.getBody();
        current.setBody(v);
        return oldBody;
    }

    /**
     * Removes the element at the specified position in this list. Shifts any subsequent
     * elements to the left (subtracts one from their indices). Returns the element that was
     * removed from the list.
     *
     * @param i the index of the element to be removed, i >= 0 && i < size().
     * @return the removed element.
     */
    public IntVar remove(int i) {
        if (head == null || i < 0 || i >= size()) return null;
        MyListNode node = head;
        for (int index = 0; index < i; index++) {
            node = node.getNext();
        }
        if (node == head) {
            return pollFirst();
        } else if (node == tail) {
            return pollLast();
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            return node.getBody();
        }
    }

    /**
     * Returns the index of the last occurrence of the specified element in this list, or -1 if
     * this list does not contain the element. More formally, returns the highest index i such
     * that v == get(i), or -1 if there is no such index.
     *
     * @param v the element to search for ('v' can also be 'null').
     * @return the index of the last occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     */
    public int lastIndexOf(IntVar v) {
        int lastIndex = -1;
        MyListNode current = head;
        int index = 0;
        while (current != null) {
            if ((current.getBody() != null && current.getBody().equals(v)) || (current.getBody() == null && v == null)) {
                lastIndex = index;
            }
            current = current.getNext();
            index++;
        }
        return lastIndex;
    }


    /**
     * Returns the number of entries in this list (including 'null' entries).
     *
     * @return the number of entries in this list (including 'null' entries).
     */
    public int size() {

        //TODO: implement method.
        int count = 0;
        MyListNode temp = head;
        while (temp != null) {
            count++;
            temp = temp.getNext();
        }
        return count;
    }

}

// TODO: define further classes, if needed (either here or in a separate file).
