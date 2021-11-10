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
                String states = line.split("=")[0].trim();
                String symbols = line.split("=")[1].trim();
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
        String context = "";
        while (fileInput.hasNext()) {
            String line = fileInput.nextLine();
            if (List.of("state", "initial", "final", "alphabet", "transition").contains(line))
                context = line;
            else
                fa.readLine(context, line);
        }
        return fa;
    }

    public boolean validate(String sequence) {
        return validateHelper(sequence, initialState.get());
    }

    private boolean validateHelper(String sequence, String currentState) {
        //System.out.println(sequence + " " + currentState);
        if (sequence.isEmpty()) {
            return finalStates.contains(currentState);
        }
        Optional<String> nextState = transitions.entrySet().stream()
                .filter(transition -> transition.getKey().getKey().equals(currentState) && transition.getValue().contains(sequence.charAt(0)))
                .map(transition -> transition.getKey().getValue())
                .findFirst();
        if (nextState.isEmpty()) {
            return false;
        }
        return validateHelper(sequence.substring(1), nextState.get());
    }

    public void cli() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String[] commandArgs = scanner.nextLine().split(" ");
            switch (commandArgs[0]) {
                case "state" -> System.out.println(states);
                case "initial" -> System.out.println(initialState.get());
                case "final" -> System.out.println(finalStates);
                case "alphabet" -> System.out.println(alphabet);
                case "transitions" -> System.out.println(transitions);
                case "check" -> System.out.println(validate(commandArgs[1]));
                case "exit" -> {
                    return;
                }
                default -> System.out.println("wrong command");
            }
        }
    }
}
