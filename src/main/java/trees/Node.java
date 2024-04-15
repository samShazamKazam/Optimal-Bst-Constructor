package trees;

public class Node<T> {
    public Node left, right = null;
    public T val;

    public Node(T val) {
        this.val = val;
    }
}
