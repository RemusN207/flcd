package main;

public class ParsingOutputEntry {
    public final Symbol symbol;
    public final int parentIndex;
    public final int rightSiblingIndex;

    public ParsingOutputEntry(Symbol symbol, int parentIndex, int rightSiblingIndex) {
        symbol = symbol.equals(Symbol.endSymbol) ? Symbol.epsilon : symbol;
        this.symbol = symbol;
        this.parentIndex = parentIndex;
        this.rightSiblingIndex = rightSiblingIndex;
    }

    public String toString(int spacing) {
        return String.format("%1$" + spacing + "s", symbol.toString()) +
                String.format("%1$" + spacing + "s", parentIndex) +
                String.format("%1$" + spacing + "s\n", rightSiblingIndex);
    }
}
