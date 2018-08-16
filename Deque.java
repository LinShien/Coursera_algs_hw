import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class Deque<Item> implements Iterable<Item> {     // 使用LinkedList，不用resize
    private int numOfItems;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;

        public Node(Node next, Node previous) {
            this.next = next;
            this.previous = previous;
        }
    }

    public Deque() {                                 // create empty deque
        first = new Node(null, null);
        last = first;
        numOfItems = 0;                             // starts from leftmost position
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {                     // number of items
        return numOfItems;          // starts from index = 0
    }

    /* first and last in addFirst and addLast mean leftmost and rightmost*/
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (first.previous == null && first.next == null && first.item == null) {        // In the beggining, index = 0 is not filled with item
            first.item = item;
            numOfItems++;
            return;
        }

        Node oldFirst = first;
        first = new Node(oldFirst, null);
        oldFirst.previous = first;                                  // set previous reference of oldFirst points to first now
        first.item = item;
        // if (last.previous == null) {last.previous = ;}           // In the begining, if addFirst first instead of addLast, the previous reference of last Node must to point to first
        numOfItems++;
    }

    /* first and last in addFirst and addLast mean leftmost and rightmost */
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (last.previous == null && last.next == null && last.item == null) {       // In the beginning, index = 0 is not filled with item
            last.item = item;
            numOfItems++;
            return;
        }

        Node oldLast = last;
        last = new Node(null, oldLast);
        last.item = item;
        oldLast.next = last;                     // 要互相refer到
        numOfItems++;
    }

    /* first and last in removeFirst and removeLast mean leftmost and rightmost(FIFO) */
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        if (!(this.first == this.last)) {        // when first and last not point to the same Node
            Item item = first.item;
            first = first.next;

            /*if (!(first.previous == null)) {           // avoid a sequence of addLast then calls a removeFirst
                first.previous = null;
            }*/
            numOfItems--;
            return item;
        } else {                               // create a empty Node
            Item item = first.item;
            first = new Node(null, null);
            last = first;
            numOfItems--;
            return item;
        }
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        if (!(this.first == this.last)) {         // when first and last not point to the same Node
            Item item = last.item;
            last = last.previous;

            if (!(last.next == null)) {           // avoid a sequence of addFirst then calls a removeLast
                last.next = null;
            }
            numOfItems--;
            return item;
        } else {
            Item item = last.item;
            last = new Node(null, null);
            first = last;
            numOfItems--;
            return item;
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node currentNode = first;
        private int numOfIters = size();

        public boolean hasNext() {
            return currentNode != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = currentNode.item;
            currentNode = currentNode.next;       // refer to the next Node even though it might be null, but hasNext() will check that
            return item;
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }

    public static void main(String[] args) {
        /* Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(0);
        deque.addFirst(1);
        deque.addFirst(2);
        Iterator<Integer> dq = deque.iterator();
        while (dq.hasNext()) {
            System.out.println(dq.next());
        } */
    }
}
