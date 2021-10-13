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
        boolean finished = false;
        while (!finished) {
            if (value.equals(current.value))
                return current.index;
            if (value.compareTo(current.value) < 0)
                if (current.left == null) {
                    current.left = new Node(value, maxIndex);
                    finished = true;
                }
                else
                    current = current.left;
            else
                if (current.right == null) {
                    current.left = new Node(value, maxIndex);
                    finished = true;
                }
                else
                    current = current.right;
        }
        maxIndex += 1;
        return maxIndex - 1;
    }
}
