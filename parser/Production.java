import java.util.*;

public class Production {
    public String lhs;
    public List<String> rhs;

    public Production(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String toString() {
        return lhs + " -> " + String.join(" ", rhs);
    }
}
