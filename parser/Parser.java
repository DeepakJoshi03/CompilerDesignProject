import java.util.*;

public class Parser {
    Map<String, Map<String, Production>> parsingTable = new HashMap<>();
    List<Production> productions;
    Set<String> nonTerminals, terminals;
    String startSymbol;
    FirstFollow ff;

    public Parser(List<Production> productions, String startSymbol) {
        this.productions = productions;
        this.startSymbol = startSymbol;
        this.ff = new FirstFollow(productions, startSymbol);
        this.nonTerminals = ff.nonTerminals;
        this.terminals = ff.terminals;
        buildTable();
    }

    private void buildTable() {
        for (String nt : nonTerminals) {
            parsingTable.put(nt, new HashMap<>());
        }

        for (Production p : productions) {
            Set<String> firstSet = getFirstOfProduction(p);
            for (String terminal : firstSet) {
                if (!terminal.equals("ε")) {
                    parsingTable.get(p.lhs).put(terminal, p);
                }
            }

            if (firstSet.contains("ε")) {
                for (String terminal : ff.getFollow(p.lhs)) {
                    parsingTable.get(p.lhs).put(terminal, p);
                }
            }
        }
    }

    private Set<String> getFirstOfProduction(Production p) {
        Set<String> result = new HashSet<>();
        for (String sym : p.rhs) {
            Set<String> first = ff.getFirst(sym);
            result.addAll(first);
            if (!first.contains("ε")) break;
        }
        return result;
    }

    public void parse(List<String> tokens) {
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(startSymbol);

        tokens.add("$");
        int index = 0;

        while (!stack.isEmpty()) {
            String top = stack.pop();
            String current = tokens.get(index);

            if (top.equals("ε")) continue;

            if (top.equals(current)) {
                index++;
            } else if (nonTerminals.contains(top)) {
                Production p = parsingTable.get(top).get(current);
                if (p == null) {
                    System.out.println("Error: Unexpected token '" + current + "'");
                    return;
                }

                List<String> rhs = new ArrayList<>(p.rhs);
                Collections.reverse(rhs);
                for (String sym : rhs) {
                    stack.push(sym);
                }
            } else {
                System.out.println("Error: Mismatched terminal '" + current + "'");
                return;
            }
        }

        if (index == tokens.size()) {
            System.out.println("Input string accepted by the grammar.");
        } else {
            System.out.println("Error: Input not fully consumed.");
        }
    }

    public void printParsingTable() {
        System.out.println("\nParsing Table:");
        for (String nt : parsingTable.keySet()) {
            for (String t : parsingTable.get(nt).keySet()) {
                System.out.println(nt + " -> " + t + " : " + parsingTable.get(nt).get(t));
            }
        }
    }
}
