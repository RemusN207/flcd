package main;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        FA fa = FA.readFromFile("dfaconstant.in");
        fa.cli();
    }
}
