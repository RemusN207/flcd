package root;

import java.util.ArrayList;
import java.util.List;

public class PIF {
    final List<PIFEntry<String, Integer>> entries;

    public PIF() {
        this.entries = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "PIF:" +  entries.stream().map(PIFEntry::toString).reduce("", (a, b) -> a + "\n" + b) + "\n";
    }
}
