package ro.flcd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    private final String filePath;
    private final List<String> reservedWords = List.of("char", "string", "int", "const", "if", "else", "read", "write", "while", "begin", "end");
    private final List<String> separators = List.of("(", ")", "[", "]", "{", "}", ";", " ");
    private final List<String> operators = List.of("+", "-", "*", "/", "<", "<=", ">=", ">", "!=", "==", "=", "%");
//    private final List<String> letters = IntStream.range(67);

    public Scanner(final String filePath, final String tokens) {
        this.filePath = Scanner.class.getClassLoader().getResource(filePath).getPath();
    }

    public void printFileContent() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                System.out.println(lineNumber + ": " + line);
                lineNumber += 1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String checkIfOperator(final String token, int index) {
        if (index >= token.length()) {
            return null;
        }
        String single = String.valueOf(token.charAt(index));
        if (index + 1 < token.length()) {
            String lookAhead = single + token.charAt(index + 1);
            if (operators.contains(lookAhead)) {
                return lookAhead;
            }
        }
        if (operators.contains(single)) {
            return single;
        }
        return null;
    }


    private boolean isWordChar(char character) {
        return Character.isLetter(character) || Character.isDigit(character) || character == '_';
    }

    private List<String> detect(final String line) {
        final List<String> initialTokens = List.of(line.split(" "));
        final List<String> correctTokens = new ArrayList<>();
        for (final String token : initialTokens) {
            int index = 0;

            while (index < token.length()) {
                final StringBuilder word = new StringBuilder();
                while (index < token.length() && isWordChar(token.charAt(index))) {
                    word.append(token.charAt(index));
                    index++;
                }
                if (!word.isEmpty()) {
                    correctTokens.add(word.toString());
                }
                if (index < token.length() && separators.contains(String.valueOf(token.charAt(index)))) {
                    correctTokens.add(String.valueOf(token.charAt(index)));
                }

                final String operator = checkIfOperator(token, index);
                if (operator != null) {
                    correctTokens.add(operator);
                    if (operator.length() > 1) {
                        index++;
                    }
                }
                index++;
            }
        }
        return correctTokens;
    }

    private boolean isValidIdentifier(final String identifier) {
        Pattern pattern = Pattern.compile("^(_|[a-zA-Z])([a-zA-Z]|[0-9])*$");
        Matcher matcher = pattern.matcher(identifier);
        return matcher.find();
    }

    private boolean isValidConstant(final String constant) {
        Pattern numberPattern = Pattern.compile("^(0|-+[1-9][0-9]*)$");
        Matcher numberMatcher = numberPattern.matcher(constant);

        Pattern stringPattern = Pattern.compile("^\"[a-zA-Z ]*\"$");
        Matcher stringMatcher = stringPattern.matcher(constant);

        Pattern charPattern = Pattern.compile("^'[a-zA-Z ]'$");
        Matcher charMatcher = charPattern.matcher(constant);
        return numberMatcher.find() || stringMatcher.find() || charMatcher.find();
    }

    private String classify(final List<String> tokens, int lineNumber) {
        //identifiers, constants, reserved words, separators, operators
        StringBuilder errors = new StringBuilder();
        for (final String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            if (!(separators.contains(token) || operators.contains(token) || reservedWords.contains(token) || isValidIdentifier(token) || isValidConstant(token))) {
                errors.append("Error on line " + lineNumber + ": the token " + token + " is invalid!\n");
            }
        }
        return errors.toString();
    }

    public void tokenize() {
        StringBuilder errors = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) { // while not eof
                List<String> tokens = detect(line);
                errors.append(classify(tokens, lineNumber));
                if (!errors.isEmpty()) {
                    System.out.println(errors);
                }
//                codify(token);
                lineNumber += 1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (errors.toString().isEmpty()) {
            System.out.println("Correct!");
        } else {
            System.out.println(errors);
        }
    }
}
