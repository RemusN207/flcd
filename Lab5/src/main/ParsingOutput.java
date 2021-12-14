package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParsingOutput {
    private final List<ParsingOutputEntry> entries;

    public ParsingOutput() {
        entries = new ArrayList<>();
    }

    public int addEntry(ParsingOutputEntry entry) {
        entries.add(entry);
        return entries.size() - 1;
    }

    public ParsingOutputEntry getEntry(int index) {
        return entries.get(index);
    }

    @Override
    public String toString() {
        AtomicInteger maxLength = new AtomicInteger(13);
        entries.stream()
                .map(entry -> entry.symbol.toString().length())
                .forEach(length -> maxLength.set(maxLength.get() > length ? maxLength.get() : length));
        maxLength.addAndGet(1);
        String finalString = String.format("%1$" + maxLength + "s", "Index") +
                String.format("%1$" + maxLength + "s", "Symbol") +
                String.format("%1$" + maxLength + "s", "Parent") +
                String.format("%1$" + maxLength + "s\n", "Right Sibling");
        return entries.stream()
                .map(entry -> String.format("%1$" + maxLength + "s", entries.indexOf(entry)) + entry.toString(maxLength.get()))
                .reduce(finalString, String::concat);
    }
}
