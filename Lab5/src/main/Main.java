package main;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Grammar g = Grammar.readFromFile("g1.txt");
        System.out.println(g.generateFollow());
        g.cli();
    }
}
