import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        int items = 0;

        if (args.length != 1) {
            throw new IllegalArgumentException("Needs k");
        }

        int k = Integer.parseInt(args[0]);

        if (k < 0) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        while (!StdIn.isEmpty()) {
            q.enqueue(StdIn.readString());
            items++;
        }

        for (int i = 0; (i < k && i < items); i++) {
            StdOut.println(q.dequeue());
        }
    }

}
