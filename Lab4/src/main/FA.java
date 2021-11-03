package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class FA {
    private final List<String> states = new ArrayList<>();
    private final AtomicReference<String> initialState = new AtomicReference<>("");
    private final List<String> finalStates = new ArrayList<>();
    private final List<Character> alphabet = new ArrayList<>();
    private final Map<Map.Entry<String, String>, List<Character>> transitions = new HashMap<>();

     private void readLine(String context, String line) {
        switch (context) {
            case "state" -> states.addAll(Stream.of(line.split(",")).map(String::trim).toList());
            case "initial" -> initialState.set(line);
            case "final" -> finalStates.addAll(Stream.of(line.split(",")).map(String::trim).toList());
            case "alphabet" -> alphabet.addAll(Stream.of(line.split(",")).map(String::trim).map(s -> s.charAt(0)).toList());
            case "transition" -> {
                String states = line.split("-")[0].trim();
                String symbols = line.split("-")[1].trim();
                transitions.put(
                        new AbstractMap.SimpleEntry<>(states.split(",")[0].trim(), states.split(",")[1].trim()),
                        Stream.of(symbols.split(",")).map(String::trim).map(s -> s.charAt(0)).toList()
                );
            }
        }
    }

    static public FA readFromFile(String filename) throws FileNotFoundException {
        FA fa = new FA();
        Scanner fileInput = new Scanner(new File(filename));
        while (fileInput.hasNext()) {
            String context = "";
            String line = fileInput.nextLine();
            if (List.of("state", "initial", "final", "alphabet", "transition").contains(line))
                context = line;
            else
                fa.readLine(context, line);
        }
        return fa;
    }


}
