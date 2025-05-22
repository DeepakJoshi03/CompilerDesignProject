import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String input = new String(Files.readAllBytes(Paths.get("input.txt")));

            Lexer lexer = new Lexer(input);
            List<Token> tokens = lexer.tokenize();

            for (Token token : tokens) {
                System.out.println(token);
            }
        } catch (IOException e) {
            System.out.println("Error reading input.txt: " + e.getMessage());
        }
    }
}
