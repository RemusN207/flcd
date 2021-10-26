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
        return toStringHelper("");
    }

    public String toStringHelper(String prefix) {
        return (left != null ? left.toStringHelper(prefix + "\t") : "") + prefix + index + " -> " + value + "\n" + (right != null ? right.toStringHelper(prefix + "\t") : "");
    }
}
