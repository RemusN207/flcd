package main;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Grammar g = Grammar.readFromFile("g2.txt");
        g.cli();
    }
}
