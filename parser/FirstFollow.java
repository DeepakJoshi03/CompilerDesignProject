import java.util.*;

public class FirstFollow {
    Map<String, Set<String>> first = new HashMap<>();
    Map<String, Set<String>> follow = new HashMap<>();
    List<Production> productions;
    Set<String> nonTerminals = new HashSet<>();
    Set<String> terminals = new HashSet<>();
    String startSymbol;

    public FirstFollow(List<Production> productions, String startSymbol) {
        this.productions = productions;
        this.startSymbol = startSymbol;
        computeSets();
    }

    private void computeSets() {
        for (Production p : productions) {
            nonTerminals.add(p.lhs);
            for (String sym : p.rhs) {
                if (!sym.equals("ε") && !Character.isUpperCase(sym.charAt(0))) {
                    terminals.add(sym);
                }
            }
        }

        for (String nt : nonTerminals) {
            first.put(nt, new HashSet<>());
            follow.put(nt, new HashSet<>());
        }

        boolean changed;
        do {
            changed = false;
            for (Production p : productions) {
                Set<String> firstSet = first.get(p.lhs);
                for (String sym : p.rhs) {
                    Set<String> symFirst = getFirst(sym);
                    boolean hasEpsilon = symFirst.contains("ε");
                    symFirst.remove("ε");

                    if (firstSet.addAll(symFirst)) changed = true;

                    if (!hasEpsilon) break;
                    if (sym.equals(p.rhs.get(p.rhs.size() - 1))) {
                        if (firstSet.add("ε")) changed = true;
                    }
                }
            }
        } while (changed);

        follow.get(startSymbol).add("$");

        do {
            changed = false;
            for (Production p : productions) {
                List<String> rhs = p.rhs;
                for (int i = 0; i < rhs.size(); i++) {
                    String B = rhs.get(i);
                    if (!nonTerminals.contains(B)) continue;

                    Set<String> followB = follow.get(B);
                    int nextIndex = i + 1;

                    Set<String> trailer = new HashSet<>();
                    if (nextIndex < rhs.size()) {
                        String next = rhs.get(nextIndex);
                        trailer = getFirst(next);
                        if (trailer.remove("ε")) {
                            trailer.addAll(follow.get(p.lhs));
                        }
                    } else {
                        trailer.addAll(follow.get(p.lhs));
                    }

                    if (followB.addAll(trailer)) changed = true;
                }
            }
        } while (changed);
    }

    public Set<String> getFirst(String symbol) {
        if (!nonTerminals.contains(symbol)) {
            return new HashSet<>(Collections.singletonList(symbol));
        }
        return first.get(symbol);
    }

    public Set<String> getFollow(String symbol) {
        return follow.get(symbol);
    }

    public void printFirstFollow() {
        System.out.println("FIRST sets:");
        for (String nt : nonTerminals) {
            System.out.println(nt + ": " + first.get(nt));
        }

        System.out.println("\nFOLLOW sets:");
        for (String nt : nonTerminals) {
            System.out.println(nt + ": " + follow.get(nt));
        }
    }
}
