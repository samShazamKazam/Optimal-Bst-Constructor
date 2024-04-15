package trees;


import trees.printer.TreePrinter;

public class OptimalBstConstructor {
    private static Node reconstruct(Node[] nodes, int[][] roots, int i, int j) {
        if (i > j) return null;

        int r = roots[i][j];
        nodes[r - 1].left = reconstruct(nodes, roots, i, r - 1);
        nodes[r - 1].right = reconstruct(nodes, roots, r + 1, j);
        return nodes[r - 1];
    }

    /**
     * Constructs an optimal BST tree from the leaves up one level at a time. It starts with considering trees of size
     * one (leaves), then it increases the size until it covers the array on Nodes. For each level, the algorithm
     * iterates over each key in the tree and presumes it is a root. The perfect root would have a minimal sum of
     * the search time of its left branch plus the search time of its right branch.
     *
     * Assuming nodes (i.e. 'i'..'j')
     * The weighted search time (W) of consecutive keys i..j is
     * W[i][j] = SUM(for k=i..j  p[k] * k's depth)
     * let 'r' be a root dividing i..j into two trees (T1 & T2) then the search time for w[i][r-1] (i.e. the left branch)
     * w[i][r-1] = SUM(for k=i..r-1  p[k] * k's depth in T1)
     * w[r+1][j] = SUM(for k=r+1..j  p[k] * k's depth in T2)
     *
     * w[i][j] = SUM(for k=i..r-1  p[k] * (k's depth in T1 + 1)) + SUM(for k=r+1..j  p[k] * (k's depth in T2 + 1) + p[r])
     * w[i][j] = SUM(for k=i..r-1  p[k] * k's depth in T1) + SUM(for k=r+1..j  p[k] * k's depth in T2) +
     *      SUM(for k=i..r-1  p[k]) + p[r] + (for k=r+1..j  p[k])
     *
     * w[i][j] = SUM(for k=i..r-1  p[k] * k's depth in T1) + SUM(for k=r+1..j  p[k] * k's depth in T2) + SUM(for k=i..j  p[k])
     * w[i][j] = W[i][r-1] + W[r+1][j] + SUM(for k=i..j  p[k])
     *
     *
     * @param p The frequency of search for each Node's value. The higher the frequency of a Node, the closer
     *             it is to the root
     * @param nodes an array of the Nodes from which a tree will be constructed, assuming they are sorted in ascending
     *              order.
     * @return the root Node of the tree
     */
    public static Node construct(double[] p, Node[] nodes) {
        if (p.length != nodes.length) {
            throw new IllegalArgumentException("Size of frequency array doesn't match the number of the nodes");
        }
        else {
            return construct_improved__(p, nodes);
        }
    }

    // O(n^3) where n is the number of nodes
    private static Node construct__(double[] p, Node[] nodes) {
        int n = p.length;
        double[][] searchTime = new double[n + 2][n + 2];
        int[][] roots = new int[n + 2][n + 2];

        for (int treeSize = 1 ; treeSize <= n; treeSize++) {
            for (int i = 1; i <= n - treeSize + 1; i++) {
                int j = i + treeSize - 1;
                double min = i > j ? 0 : Double.MAX_VALUE;
                double sum = 0;
                int bestRoot = -1;
                for (int r = i; r <= j; r++) {
                    if (searchTime[i][r - 1] + searchTime[r + 1][j] < min) {
                        min = searchTime[i][r - 1] + searchTime[r + 1][j];
                        bestRoot = r;
                    }
                    sum += p[r - 1];
                }

                roots[i][j] = bestRoot;
                searchTime[i][j] = sum + min;
            }
        }

        return reconstruct(nodes, roots, 1, n);
    }


    /**
     * because of the monotonicity property, we can improve the time to O(n^2).
     * Since the root of keys [i..j] can be at least the root of keys[i..j-1] and
     * at most the root of keys[i+1..j]. This was found by D. Knuth in the 1970s.
     *
     * It also uses prefixes to calculate the sum of frequencies from [i..j]
     * @param p
     * @param nodes
     * @return
     */
    private static Node construct_improved__(double[] p, Node[] nodes) {
        int n = p.length;

        double[] prefix= findPrefixes(p);

        double[][] searchTime = new double[n + 2][n + 2];
        int[][] roots = new int[n + 2][n + 2];

        for (int treeSize = 1 ; treeSize <= n; treeSize++) {
            for (int i = 1; i <= n - treeSize + 1; i++) {
                int j = i + treeSize - 1;
                double min = i > j ? 0 : Double.MAX_VALUE;
                int bestRoot = -1;

                int leastRoot = i < j ? roots[i][j-1] : i; // when i == j, root should be i at least
                int highestRoot = i < j ? roots[i+1][j] : j; // when i == j, root should be j at most

                for (int r = leastRoot; r <= highestRoot; r++) {
                    if (searchTime[i][r - 1] + searchTime[r + 1][j] < min) {
                        min = searchTime[i][r - 1] + searchTime[r + 1][j];
                        bestRoot = r;
                    }
                }

                roots[i][j] = bestRoot;
                searchTime[i][j] = prefix[j] - prefix[i - 1] + min;
            }
        }

        return reconstruct(nodes, roots, 1, n);
    }

    private static double[] findPrefixes(double[] p) {
        double[] prefix = new double[p.length + 1];
        prefix[0] = 0;

        for (int i = 1 ; i <= p.length ; i++) {
            prefix[i] =  prefix[i-1] + p[i - 1];
        }
        return prefix;
    }


    public static void main(String[] args) {
        int n = 4;
        double[] freq = new double[]{0.8, 0.1, 0.6, 0.5};
        Node<Character>[] nodes = new Node[n];
        for (int i = 0 ; i < n ; i++) {
            nodes[i] = new Node((char) ('a' + i));
        }
        TreePrinter.print(OptimalBstConstructor.construct(freq, nodes), System.out);
    }
}
