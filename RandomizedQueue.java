import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {     //implementation with resizing array
    private int numOfItems;
    private Item[] arr;
    private int head;                     // the lowest index filled with a item
    private int tail;

    public RandomizedQueue() {               // construct an empty randomized queue
        numOfItems = 0;
        head = 0;
        tail = 0;
        arr = (Item[]) new Object[10];
    }

    public boolean isEmpty() {
        return numOfItems == 0;
    }

    public int size() {
        return numOfItems;
    }

    /* worst case is linear time proportional to N items but amortized time is constant time*/
    public void enqueue(Item item) {
        if (item == null) { throw new IllegalArgumentException(); }

        if (arr.length == 0) {
            arr = (Item[]) new Object[10];
        }

        if (isAboutToFull()) {                                    // resize the array
            resizing(arr.length * 2);
        }

        if (tail == 0 && arr[tail] == null) {            // In the beginning, tail = head = 0
            arr[tail] = item;
        } else {
            tail += 1;
            arr[tail] = item;
        }
        numOfItems++;
    }

    /* best case and worst case both are linear */
    public Item dequeue() {
        if (isEmpty()) {throw new NoSuchElementException();}

        if (size() == 1) {
            Item item = arr[head];
            arr = (Item[]) new Object[]{};
            numOfItems = 0;
            return item;
        }
        int randIndex = StdRandom.uniform(tail + 1);
        Item item = arr[randIndex];                             // pop first, then resize
        arr[randIndex] = null;
        numOfItems -= 1;
        if (size() <= arr.length / 4 && arr.length / 2 > 0) {   // arr.length / 2 > 0 ===> to exclude length <= 1
            resizing(arr.length / 2);
        } else {
            resizing(size());
        }

        tail = size() - 1;
        head = 0;
        return item;
    }

    public Item sample() {
        if (isEmpty()) {throw new NoSuchElementException();}

        int randIndex = StdRandom.uniform(head, tail + 1);
        return arr[randIndex];
    }

    public Iterator<Item> iterator() {return new RandomizedQueueIterator();}

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] items;
        private int currentIndex;

        public RandomizedQueueIterator() {
            /* construct a Deque object and copy the arr to it takes linear time proportional to N items */
            items = (Item[]) new Object[size()];
            currentIndex = (size() == 0) ? -1 : size() -1;    // when the Rq is empty, set the currentIndex to -1


            for (int i = 0; i <= tail; i++) {
                items[i] = arr[i];
            }
            /* uniformly random order */
            StdRandom.shuffle(items);
        }

        @Override
        public boolean hasNext() {
            return currentIndex >= 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {throw new NoSuchElementException();}
            Item item = items[currentIndex];
            currentIndex--;
            return item;
        }

        public void remove() {throw new UnsupportedOperationException();}
    }


    private boolean isAboutToFull() {
        return (numOfItems + 1) >= arr.length;
    }

    private void resizing(int length) {
        //if (length == 0) {arr = new Object[]{};}
        Item[] newArr = (Item[]) new Object[length];
        int newIndex = 0;

        for(int i = 0; i <= tail; i++) {    // scan the old arr from head to tail
            if(arr[i] != null) {
                newArr[newIndex] = arr[i];
                newIndex++;
            }
        }
        arr = newArr;
    }

    public static void main(String[] args) {
         /*RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(692);
        rq.enqueue(802);
        rq.enqueue(747);
        rq.enqueue(612);
        rq.enqueue(333);
        rq.enqueue(692);
        rq.enqueue(802);
        rq.enqueue(747);
        rq.enqueue(612);
        rq.enqueue(333);
        rq.enqueue(692);
        rq.enqueue(802);
        rq.enqueue(747);
        rq.enqueue(612);
        rq.enqueue(333);
        rq.enqueue(692);
        rq.enqueue(802);
        rq.enqueue(747);
        rq.enqueue(612);
        rq.enqueue(333);
        rq.enqueue(692);
        Iterator<Integer> r = rq.iterator();
        while (r.hasNext()) {
            System.out.println(r.next());
        } */
    }
}