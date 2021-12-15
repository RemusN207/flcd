package main;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Grammar g = Grammar.readFromFile("g1.txt");
        System.out.println(g.parseInput(List.of(
                new Symbol("a"),
                new Symbol("+"),
                new Symbol("a"))));
        /*var p = new ParsingOutput();
        p.addEntry(new ParsingOutputEntry(new Terminal("A"), -1, -1));
        p.addEntry(new ParsingOutputEntry(new Terminal("B"), 0, -1));
        System.out.println(p);*/
    }
}
