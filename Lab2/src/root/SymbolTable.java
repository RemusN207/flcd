package root;

public class SymbolTable {
    private Node head;
    private int maxIndex;

    SymbolTable() {
        head = null;
        maxIndex = 0;
    }

    int getIndex(String value) {
        if (head == null) {
            head = new Node(value, maxIndex);
            maxIndex += 1;
        }
        Node current = head;
        while (!value.equals(current.value)) {
            if (value.compareTo(current.value) < 0) {
                if (current.left == null) {
                    current.left = new Node(value, maxIndex);
                    maxIndex += 1;
                }
                current = current.left;
            }
            if (value.compareTo(current.value) > 0) {
                if (current.right == null) {
                    current.right = new Node(value, maxIndex);
                    maxIndex += 1;
                }
                current = current.right;
            }
        }
        return current.index;
    }
}
