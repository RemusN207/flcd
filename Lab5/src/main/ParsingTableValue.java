package main;

import java.util.ArrayList;
import java.util.List;

public class ParsingTableValue {
    static final ParsingTableValue pop = new ParsingTableValue(new ArrayList<>(), 0);
    static final ParsingTableValue acc = new ParsingTableValue(new ArrayList<>(), -1);

    public final List<Symbol> production;
    public final int productionIndex;

    public ParsingTableValue(List<Symbol> production, int productionIndex) {
        this.production = production;
        this.productionIndex = productionIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ParsingTableValue))
            return false;
        return ((ParsingTableValue) obj).production.equals(this.production)
                && ((ParsingTableValue) obj).productionIndex == this.productionIndex;
    }

    @Override
    public String toString() {
        if (this.equals(ParsingTableValue.acc))
            return "acc";
        if (this.equals(ParsingTableValue.pop))
            return "pop";
        return "(" + production + ", " + productionIndex + ")";
    }
}
