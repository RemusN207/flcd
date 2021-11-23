package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grammar {
    private final List<Symbol> symbols = new ArrayList<>();
    private final Map<Symbol, List<List<Symbol>>> productions = new HashMap<>();

    private Symbol getSymbol(String content) {
        return symbols.get(symbols.indexOf(new Symbol(content)));
    }

    private void readLine(String context, String line) {
        switch (context) {
            case "terminal" -> symbols.addAll(Stream.of(line.split(",")).map(String::trim).map(Terminal::new).toList());
            case "nonTerminal" -> symbols.addAll(Stream.of(line.split(",")).map(String::trim).map(NonTerminal::new).toList());
            case "production" -> {
                String leftSide = line.split("->")[0].trim();
                if (!symbols.contains(new NonTerminal(leftSide))) {
                    System.out.println("NON CFG: " + leftSide);
                    break;
                }
                String rightSide = line.split("->")[1].trim();
                List<List<Symbol>> prods = Stream.of(rightSide.split("\\|"))
                        .map(String::trim).map(prod -> Stream.of(prod.split(" "))
                                .map(this::getSymbol)
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList());
                productions.put(getSymbol(leftSide), prods);
            }
        }
    }

    static public Grammar readFromFile(String filename) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.symbols.add(Symbol.epsilon);
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
                case "productionFor" -> System.out.println(productions.get(getSymbol(commandArgs[1])));
                case "exit" -> {
                    return;
                }
                default -> System.out.println("wrong command");
            }
        }
    }

    private static Set<Symbol> ConcatOfLen1(Set<Symbol> a, Set<Symbol> b){
        if (b.isEmpty())
            return Set.of();
        return a.stream().flatMap(symbol -> {
            if (symbol.equals(Symbol.epsilon))
                return b.stream();
            return Stream.of(symbol);
        }).collect(Collectors.toSet());
    }

    public Map<Symbol, Set<Symbol>> generateFirst() {
        Map<Symbol, Set<Symbol>> first = new HashMap<>();
        symbols.forEach(symbol -> {
            first.put(symbol, new HashSet<>());
            if (symbol instanceof Terminal || symbol.equals(Symbol.epsilon))
                first.get(symbol).add(symbol);
        });
        AtomicBoolean convergent = new AtomicBoolean(false);
        while(!convergent.get()) {
            convergent.set(true);
            symbols.stream().filter(symbol -> symbol instanceof NonTerminal)
                    .forEach(symbol -> {
                        int prevLen = first.get(symbol).size();
                        first.get(symbol).addAll(
                                productions.get(symbol).stream()
                                        .flatMap(prod -> prod.stream()
                                                .map(first::get)
                                                .reduce(Grammar::ConcatOfLen1)
                                                .get().stream()
                                        ).collect(Collectors.toSet())
                        );
                        if (first.get(symbol).size() != prevLen)
                            convergent.set(false);
                    });
        }
        return first;
    }
}
