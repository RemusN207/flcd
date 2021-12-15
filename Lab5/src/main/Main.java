package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<Symbol> ParseInputFile(String filename) throws FileNotFoundException {
        List<Symbol> symbols = new ArrayList<>();
        Scanner fileInput = new Scanner(new File(filename));
        while(fileInput.hasNext())
        {
            String line = fileInput.nextLine();
            var splitLine= line.split(",");

            symbols.add(new Symbol(splitLine[0]));
        }
        return symbols;
    }
    public static void main(String[] args) throws IOException {
        Grammar g = Grammar.readFromFile("g1.txt");
        BufferedWriter fileOutput = new BufferedWriter(new FileWriter("out1.txt"));
        fileOutput.write(g.parseInput(ParseInputFile("seq.txt")).toString());
        fileOutput.flush();
        g = Grammar.readFromFile("g2.txt");
        fileOutput = new BufferedWriter(new FileWriter("out2.txt"));
        fileOutput.write(g.parseInput(ParseInputFile("PIF.out")).toString());
        fileOutput.flush();
    }
}
