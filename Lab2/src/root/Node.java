package root;

public class Node {
    final String value;
    final int index;
    Node left, right;

    public Node(String value, int index) {
        this.value = value;
        this.index = index;
        left = null;
        right = null;
    }

    @Override
    public String toString() {
        return (left != null ? left.toString() : "") + index + ", " + value + "\n" + (right != null ? right.toString() : "");
    }
}
