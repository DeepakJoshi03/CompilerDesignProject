import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Production> productions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("grammar.txt"));

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("->");
            String lhs = parts[0].trim();
            String[] rhsParts = parts[1].trim().split("\\|");
            for (String alt : rhsParts) {
                List<String> rhs = Arrays.asList(alt.trim().split("\\s+"));
                productions.add(new Production(lhs, rhs));
            }
        }

        String startSymbol = productions.get(0).lhs;

        Parser parser = new Parser(productions, startSymbol);
        parser.ff.printFirstFollow();
        parser.printParsingTable();

        System.out.println("\nEnter input string (tokens separated by space):");
        BufferedReader inputReader = new BufferedReader(new FileReader("input.txt"));
        String inputLine = inputReader.readLine();
        List<String> tokens = Arrays.asList(inputLine.trim().split("\\s+"));

        parser.parse(new ArrayList<>(tokens));
    }
}
