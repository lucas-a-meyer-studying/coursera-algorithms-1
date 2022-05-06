import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first = null;
    private Node last = null;
    private int itemCount;

    private class Node {
        Item item;
        Node previous;
        Node next;
    }

    // construct an empty deque
    public Deque() {
        itemCount = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return itemCount == 0;
    }

    // return the number of items on the deque
    public int size() {
        return itemCount;
    }

    // add the item to the front
    public void addFirst(Item item) {

        if (item == null) {
            throw new IllegalArgumentException();
        }
        itemCount++;
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;

        if (itemCount <= 1) {
            last = first;
        } else {
            first.next.previous = first;
        }

    }

    // add the item to the back
    public void addLast(Item item) {

        if (item == null) {
            throw new IllegalArgumentException();
        }

        itemCount++;
        Node oldLast = last;
        last = new Node();
        last.next = null;
        last.item = item;
        last.previous = oldLast;

        if (itemCount <= 1) {
            first = last;
        } else {
            last.previous.next = last;
        }

    }

    // remove and return the item from the front
    public Item removeFirst() {

        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        itemCount--;
        Item itemToReturn = first.item;
        first = first.next;

        if (first != null)
            first.previous = null;

        if (itemCount == 1) {
            first = last;
        }

        if (itemCount == 0) {
            first = null;
            last = null;
        }

        return itemToReturn;
    }

    // remove and return the item from the back
    public Item removeLast() {

        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        itemCount--;

        Item itemToReturn = last.item;
        last = last.previous;

        if (last != null)
            last.next = null;

        if (itemCount == 1) {
            last = first;
        }

        if (itemCount == 0) {
            first = null;
            last = null;
        }

        return itemToReturn;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addLast(1);
        StdOut.printf("%d\n", deque.removeFirst());
        deque.addLast(3);
        StdOut.printf("%d\n", deque.removeLast());

        Iterator<Integer> i = deque.iterator();
        StdOut.printf("%d\n", i.next());
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
