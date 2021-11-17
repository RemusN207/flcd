package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grammar {
    private final List<Symbol> symbols = new ArrayList<>();
    private final Map<Integer, List<List<Integer>>> productions = new HashMap<>();

    private void readLine(String context, String line) {
        switch (context) {
            case "terminal" -> symbols.addAll(Stream.of(line.split(",")).map(String::trim).map(Terminal::new).toList());
            case "nonTerminal" -> symbols.addAll(Stream.of(line.split(",")).map(String::trim).map(NonTerminal::new).toList());
            case "production" -> {
                String leftSide = line.split("->")[0].trim();
                if (!symbols.contains(new NonTerminal(leftSide))) {
                    System.out.println("NON CFG");
                    break;
                }
                String rightSide = line.split("->")[1].trim();
                List<List<Integer>> prods = Stream.of(rightSide.split("\\|")).map(String::trim).map(prod -> {
                    AtomicReference<String> buffer = new AtomicReference<>("");
                    List<Integer> result = new ArrayList<>();
                    prod.chars().forEach(chr -> {
                        buffer.set(buffer.get() + (char)chr);
                        System.out.println(buffer.get());
                        int index = symbols.indexOf(new Symbol(buffer.get()));
                        if (index != -1) {
                            buffer.set("");
                            result.add(index);
                        }
                    });
                    if (buffer.get().equals(""))
                        return result;
                    return new ArrayList<Integer>();
                }).collect(Collectors.toList());
                productions.put(symbols.indexOf(new NonTerminal(leftSide)), prods);
            }
        }
    }

    static public Grammar readFromFile(String filename) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        Scanner fileInput = new Scanner(new File(filename));
        String context = "";
        while (fileInput.hasNext()) {
            String line = fileInput.nextLine();
            if (List.of("terminal", "nonTerminal", "production").contains(line))
                context = line;
            else
                grammar.readLine(context, line);
        }
        return grammar;
    }

    public void cli() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String[] commandArgs = scanner.nextLine().split(" ");
            switch (commandArgs[0]) {
                case "terminal" -> System.out.println(symbols.stream().filter(s -> s instanceof Terminal).toList());
                case "nonTerminal" -> System.out.println(symbols.stream().filter(s -> s instanceof NonTerminal).toList());
                case "production" -> System.out.println(productions);
                case "productionFor" -> System.out.println(productions.get(symbols.indexOf(new Symbol(commandArgs[1]))));
                case "exit" -> {
                    return;
                }
                default -> System.out.println("wrong command");
            }
        }
    }
}
