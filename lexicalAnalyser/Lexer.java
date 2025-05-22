import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int pos;
    private int length;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.length = input.length();
    }

    private char peek() {
        if (pos < length) {
            return input.charAt(pos);
        }
        return '\0';
    }

    private char advance() {
        if (pos < length) {
            return input.charAt(pos++);
        }
        return '\0';
    }

    private boolean isLetter(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < length) {
            char current = peek();

            if (Character.isWhitespace(current)) {
                advance();
                continue;
            }

            if (isLetter(current)) {
                StringBuilder sb = new StringBuilder();
                while (isLetter(peek()) || isDigit(peek())) {
                    sb.append(advance());
                }
                String word = sb.toString();
                if (isKeyword(word)) {
                    tokens.add(new Token(TokenType.KEYWORD, word));
                } else {
                    tokens.add(new Token(TokenType.IDENTIFIER, word));
                }
            } else if (isDigit(current)) {
                StringBuilder sb = new StringBuilder();
                while (isDigit(peek())) {
                    sb.append(advance());
                }
                tokens.add(new Token(TokenType.NUMBER, sb.toString()));
            } else if (isOperator(current)) {
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(advance())));
            } else if (isSeparator(current)) {
                tokens.add(new Token(TokenType.SEPARATOR, String.valueOf(advance())));
            } else {
                // Unknown character, skip or throw error
                advance();
            }
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private boolean isKeyword(String word) {
        String[] keywords = {"int", "float", "if", "else", "while", "return", "void", "main"};
        for (String k : keywords) {
            if (k.equals(word)) return true;
        }
        return false;
    }

    private boolean isOperator(char c) {
        return "+-*/=<>!".indexOf(c) != -1;
    }

    private boolean isSeparator(char c) {
        return "();{}".indexOf(c) != -1;
    }
}
