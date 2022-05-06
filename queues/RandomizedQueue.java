import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int itemCount;
    private int allocatedSize;
    private int endPointer;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        allocatedSize = 1;
        endPointer = 0;
        itemCount = 0;

        items = (Item[]) new Object[allocatedSize];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return itemCount == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return itemCount;
    }

    // add the item
    public void enqueue(Item item) {

        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (itemCount == allocatedSize) {
            resize(allocatedSize * 2);
        }
        items[endPointer] = item;
        endPointer++;
        itemCount++;
    }

    // remove and return a random item
    public Item dequeue() {

        if (itemCount == 0) {
            throw new NoSuchElementException();
        }

        int pos = StdRandom.uniform(itemCount);
        Item i = items[pos];
        endPointer--;
        items[pos] = items[endPointer];
        items[endPointer] = null;
        itemCount--;
        if ((itemCount <= allocatedSize / 4) && (allocatedSize > 2))
            resize(allocatedSize / 2);
        return i;
    }

    // return a random item (but do not remove it)
    public Item sample() {

        if (itemCount == 0) {
            throw new NoSuchElementException();
        }

        int pointer = StdRandom.uniform(itemCount);
        return items[pointer];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        Item[] iteratorItems = items.clone();
        int iteratorPointer = endPointer;
        int iteratorCount = itemCount;

        public boolean hasNext() {
            return iteratorCount > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            int pos = StdRandom.uniform(iteratorCount);
            Item i = iteratorItems[pos];
            iteratorPointer--;
            iteratorItems[pos] = iteratorItems[iteratorPointer];
            iteratorCount--;

            return i;
        }
    }

    private void resize(int newCapacity) {
        Item[] copy = (Item[]) new Object[newCapacity];
        for (int i = 0; i < itemCount; i++)
            copy[i] = items[i];
        items = copy;
        allocatedSize = newCapacity;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();

        for (int i = 0; i < 10; i++)
            q.enqueue(i);


        for (Integer item1 : q)
            for (Integer item2 : q) {
                StdOut.printf("%d\n", item1);
                StdOut.printf("%d\n", item2);
            }

        StdOut.println("=============================================");

        for (Integer item1 : q) {
            StdOut.printf("%d\n", item1);
        }


        for (Integer item2 : q) {
            StdOut.printf("%d\n", item2);
        }

    }

}
