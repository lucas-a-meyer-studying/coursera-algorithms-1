import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // private Stack<Node> gameNodes;
    private Node finalNode;
    private boolean solvedInitial = false;
    private int solMoves;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        MinPQ<Node> myPQ = new MinPQ<>();
        MinPQ<Node> twinPQ = new MinPQ<>();
        Node current = new Node(initial, null, 0);
        Node twinNode = new Node(current.board.twin(), null, 0);

        do {
            if (current.isGoal()) {
                finalNode = current;
                solMoves = current.moves;
                solvedInitial = true;
                break;
            }
            if (twinNode.isGoal()) {
                finalNode = null;
                solMoves = -1;
                solvedInitial = false;
                break;
            }
            for (Board b : current.neighbors()) {
                if (current.previous == null) {
                    Node n = new Node(b, current, current.moves + 1);
                    myPQ.insert(n);
                }
                else if (!b.equals(current.previous.board)) {
                    Node n = new Node(b, current, current.moves + 1);
                    myPQ.insert(n);
                }
            }
            for (Board b : twinNode.neighbors()) {
                if (twinNode.previous == null) {
                    Node n = new Node(b, twinNode, twinNode.moves + 1);
                    twinPQ.insert(n);
                }
                else if (!b.equals(twinNode.previous.board)) {
                    Node n = new Node(b, twinNode, twinNode.moves + 1);
                    twinPQ.insert(n);
                }
            }
            current = myPQ.delMin();
            twinNode = twinPQ.delMin();
        } while ((current != null) && (twinNode != null));

    }

    private class Node implements Comparable<Node> {
        private final Node previous;
        private final Board board;
        private final int moves;
        private final int priority;
        private boolean goal = false;

        public Node(Board board, Node previous, int moves) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            int dist = board.manhattan();
            this.goal = (dist == 0);
            this.priority = dist + moves;
        }

        public boolean isGoal() {
            return goal;
        }

        public Iterable<Board> neighbors() {
            return board.neighbors();
        }

        public int compareTo(Node that) {
            return this.priority - that.priority;
        }

        public String toString() {
            return board.toString();
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvedInitial;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Node solutionSequence = finalNode;
        if (finalNode == null) {
            return null;
        }

        Stack<Board> sol = new Stack<>();
        while (solutionSequence != null) {
            sol.push(solutionSequence.board);
            solutionSequence = solutionSequence.previous;
        }

        return sol;
    }

    // test client
    public static void main(String[] args) {
        int n = StdIn.readInt();
        StdOut.printf("Reading a %dx%d board\n", n, n);
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = StdIn.readInt();
            }
        }

        Board initial = new Board(tiles);

        StdOut.println("Trying to solve:");
        StdOut.println(initial);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable()) StdOut.println("No solution possible");
        else {
            StdOut.printf("Solution in %d moves:\n", solver.moves());
            for (Board b : solver.solution())
                StdOut.println(b);
        }

    }

}
