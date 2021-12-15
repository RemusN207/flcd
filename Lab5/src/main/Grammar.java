package main;

import jdk.swing.interop.SwingInterOpUtils;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grammar {
    private final List<Symbol> symbols = new ArrayList<>();
    private final Map<Symbol, List<List<Symbol>>> productions = new HashMap<>();
    private Map<Symbol, Set<Symbol>> first = null;
    private Map<Symbol, Set<Symbol>> follow = null;
    private Map<Map.Entry<Symbol, List<Symbol>>, Integer> productionIndexMap = null;
    private Map<Map.Entry<Symbol, Symbol>, ParsingTableValue> parsingTable = null;
    private Symbol startingSymbol = new Symbol("");

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
                                .toList())
                        .toList();
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
        grammar.startingSymbol = grammar.symbols.stream().filter(symbol -> symbol instanceof NonTerminal).findFirst().get();
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
        first = new HashMap<>();
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

    public Map<Symbol, Set<Symbol>> generateFollow() {
        if (first == null)
            generateFirst();

        follow = new HashMap<>();
        symbols.stream().filter(symbol -> symbol instanceof NonTerminal)
                .forEach(nonTerminal -> follow.put(nonTerminal, new HashSet<>()));
        follow.get(startingSymbol).add(Symbol.epsilon);

        AtomicBoolean convergent = new AtomicBoolean(false);
        while(!convergent.get()) {
            convergent.set(true);
            symbols.stream().filter(symbol -> symbol instanceof NonTerminal)
                    .forEach(symbol -> {
                        int prevLen = follow.get(symbol).size();
                        productions.entrySet().stream().flatMap(productions ->
                                productions.getValue().stream().map(production -> Map.entry(productions.getKey(), production)))
                                .filter(production -> production.getValue().contains(symbol))
                                .forEach(production -> IntStream.range(0, production.getValue().size())
                                        .filter(index -> symbol.equals(production.getValue().get(index)))
                                        .forEach(index -> {
                                            if (index == production.getValue().size() - 1 ||
                                                    first.get(production.getValue().get(index + 1)).contains(Symbol.epsilon))
                                                follow.get(symbol).addAll(follow.get(production.getKey()));
                                            if (index < production.getValue().size() - 1)
                                                follow.get(symbol).addAll(first.get(production.getValue().get(index + 1)));
                                        }));
                        if (follow.get(symbol).size() != prevLen)
                            convergent.set(false);
                    });
        }
        return follow;
    }

    private void generateProductionIndexes() {
        productionIndexMap = new HashMap<>();
        AtomicInteger productionIndex = new AtomicInteger(1);
        productions.entrySet().stream()
                .flatMap(productions -> productions.getValue().stream()
                        .map(production -> Map.entry(productions.getKey(), production)))
                .forEach(production -> {
                    productionIndexMap.put(production, productionIndex.get());
                    productionIndex.addAndGet(1);
                });
    }

    private void putInParsingTable(Symbol symbol1, Symbol symbol2, ParsingTableValue tableValue) {
        if (parsingTable.containsKey(Map.entry(symbol1, symbol2))
                && !parsingTable.get(Map.entry(symbol1, symbol2)).equals(tableValue))
            System.out.println("COLLISION!");
        parsingTable.put(Map.entry(symbol1, symbol2), tableValue);
    }

    public Map<Map.Entry<Symbol, Symbol>, ParsingTableValue> generateTable() {
        if (follow == null)
            generateFollow();
        if (productionIndexMap == null)
            generateProductionIndexes();

        parsingTable = new HashMap<>();
        productions.entrySet().stream()
                .flatMap(productions -> productions.getValue().stream()
                        .map(production -> Map.entry(productions.getKey(), production)))
                .forEach(production -> {
                    production.getValue().stream()
                            .map(symbol -> first.get(symbol))
                            .reduce(Grammar::ConcatOfLen1).get()
                            .forEach(a -> {
                                if (!a.equals(Symbol.epsilon)) {
                                    putInParsingTable(production.getKey(), a, new ParsingTableValue(production.getValue(), productionIndexMap.get(production)));
                                } else {
                                    follow.get(production.getKey())
                                            .forEach(b -> {
                                                b = b.equals(Symbol.epsilon) ? Symbol.endSymbol : b;
                                                putInParsingTable(production.getKey(), b, new ParsingTableValue(production.getValue(), productionIndexMap.get(production)));
                                            });
                                }
                            });
                });
        symbols.stream()
                .filter(symbol -> symbol instanceof Terminal || symbol.equals(Symbol.epsilon))
                .forEach(terminal -> putInParsingTable(terminal, terminal, ParsingTableValue.pop));
        putInParsingTable(Symbol.endSymbol, Symbol.endSymbol, ParsingTableValue.acc);

        return parsingTable;
    }

    public ParsingOutput parseInput(List<Symbol> input) {
        if (parsingTable == null)
            generateTable();
        ParsingOutput output = new ParsingOutput();
        output.addEntry(new ParsingOutputEntry(startingSymbol, -1, -1));
        List<Symbol> inputStack = new ArrayList<>(input);
        inputStack.add(Symbol.endSymbol);
        List<Symbol> workingStack = new ArrayList<>(List.of(startingSymbol, Symbol.endSymbol));
        int workingOutputIndex = 0;

        while (true) {
            Optional<Map.Entry<Symbol, Symbol>> parsingTableKey = parsingTable.keySet().stream()
                    .filter(key2 -> key2.equals(Map.entry(workingStack.get(0), inputStack.get(0))))
                    .findFirst();
            if (parsingTableKey.isEmpty()) {
                System.out.println("error at " + Map.entry(workingStack.get(0), inputStack.get(0)));
                return null;
            }
            if (parsingTable.get(parsingTableKey.get()).equals(ParsingTableValue.acc))
                return output;
            if (parsingTable.get(parsingTableKey.get()).equals(ParsingTableValue.pop)) {
                inputStack.remove(0);
                workingStack.remove(0);
                while (output.getRightSiblingOf(workingOutputIndex) == -1) {
                    workingOutputIndex = output.getEntry(workingOutputIndex).parentIndex;
                    if (workingOutputIndex == 0)
                        break;
                }
                if (workingOutputIndex != 0)
                    workingOutputIndex = output.getRightSiblingOf(workingOutputIndex);
                continue;
            }
            List<Symbol> expanse = parsingTable.get(parsingTableKey.get()).production;
            if (expanse.equals(List.of(Symbol.epsilon)))
                inputStack.add(0, Symbol.epsilon);
            workingStack.remove(0);
            workingStack.addAll(0, expanse);
            AtomicInteger workingOutputIndexWrapper = new AtomicInteger(workingOutputIndex);
            AtomicInteger rightSibling = new AtomicInteger(-1);
            AtomicInteger newWorkingOutputIndex = new AtomicInteger(-1);
            expanse.forEach(symbol -> {
                rightSibling.set(output.addEntry(new ParsingOutputEntry(symbol, workingOutputIndexWrapper.get(), rightSibling.get())));
                if (newWorkingOutputIndex.get() == -1)
                    newWorkingOutputIndex.set(rightSibling.get());
            });
            workingOutputIndex = newWorkingOutputIndex.get();
        }
    }
}
