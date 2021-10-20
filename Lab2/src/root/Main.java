package root;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        PIF pif = new PIF();
        SymbolTable sl = new SymbolTable();
        Scanner tokenInput = new Scanner(new File("token.in"));
        Scanner codeFile = new Scanner(new File("code.in"));
        PrintWriter pifOut = new PrintWriter("pif.out");
        PrintWriter slOut = new PrintWriter("sl.out");

        List<String> tokens = new ArrayList<>();
        while (tokenInput.hasNext()) {
            tokens.add(tokenInput.nextLine());
        }

        Pattern tokenizer = Pattern.compile("\\s*[a-zA-Z0-9_\"]+\\s*|\\s*[^a-zA-Z0-9_\";\\s]+\\s*|\\s*;+\\s*");
        int lineNo = 1;
        StringBuilder errors = new StringBuilder();
        while (codeFile.hasNext()) {
            String line = codeFile.nextLine();
            Matcher matcher = tokenizer.matcher(line);
            while(matcher.find()) {
                String token = matcher.group().trim();
                if(tokens.contains(token)) {
                    pif.entries.add(new PIFEntry<>(token, -1));
                } else {
                        if (IsSymbol(token).equals("none"))
                            errors.append("Illegal token \" ").append(token).append(" \" at line ").append(lineNo).append("\n");
                        else
                            pif.entries.add(new PIFEntry<>(IsSymbol(token), sl.getIndex(token)));
                }
            }
            lineNo += 1;
        }
        if(errors.isEmpty()) {
            System.out.println("Lexically correct!");
            pifOut.print(pif);
            slOut.print(sl);
        }
        else
            System.out.println(errors);
        pifOut.close();
        slOut.close();
    }

    public static String IsSymbol(String token) {
        Matcher identifier = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*").matcher(token);
        Matcher constant = Pattern.compile("0|[1-9][0-9]*|\"[a-zA-Z0-9_]*\"").matcher(token);
        if(identifier.matches())
            return "identifier";
        if(constant.matches())
            return "constant";
        return "none";
    }
}
