package ro.flcd;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    private final String filePath;
    private final String tokensPath;
    private final String pifPath;
    private final String stPath;
    private final ProgramInternalForm pif = new ProgramInternalForm();
    private final SymbolTable symbolTable = new SymbolTable();
    private String identifierRegex;
    private List<String> constantRegex;
    private String numericalRegex;
    private List<String> reservedWords = List.of("char", "string", "int", "const", "if", "else", "read", "write", "while", "begin", "end");
    private List<String> separators = new ArrayList<>(List.of("(", ")", "[", "]", "{", "}", ";", " "));
    private List<String> operators = List.of("+", "-", "*", "/", "<", "<=", ">=", ">", "!=", "==", "=", "%");
    private String errors;

    public Scanner(final String filePath, final String tokensPath, final String pifPath, final String stPath) {
        this.filePath = Scanner.class.getClassLoader().getResource(filePath).getPath();
        this.tokensPath = Scanner.class.getClassLoader().getResource(tokensPath).getPath();
        this.pifPath = Scanner.class.getClassLoader().getResource(pifPath).getPath();
        this.stPath = Scanner.class.getClassLoader().getResource(stPath).getPath();
        this.loadTokens();
    }

    private void loadTokens() {
        try (BufferedReader br = new BufferedReader(new FileReader(tokensPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> parts = List.of(line.split(":"));
                if (parts.size() != 2) {
                    throw new RuntimeException("Problem with token.in: " + parts);
                }
                List<String> tokens = new ArrayList<>(List.of(parts.get(1).split(",")));
                switch (parts.get(0)) {
                    case "separator" -> {
                        this.separators = tokens;
                        this.separators.sort(Comparator.comparingInt(String::length).reversed());
                    }
                    case "operator" -> {
                        this.operators = tokens;
                        this.operators.sort(Comparator.comparingInt(String::length).reversed());
                    }
                    case "reserved" -> this.reservedWords = tokens;
                    case "constant" -> {
                        this.constantRegex = tokens;
                        this.numericalRegex = tokens.get(0);
                    }
                    case "identifier" -> this.identifierRegex = tokens.get(0);
                    default ->
                            throw new RuntimeException("Problem with token.in: Invalid type of token: " + parts.get(0));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProgramInternalForm getPif() {
        return pif;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public String getErrors() {
        return errors;
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
        final List<String> initialTokens = List.of(line.split("((?<= )|(?= ))"));
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
        if (correctTokens.contains("\"") || correctTokens.contains("'")) {
            int index = 0;
            List<String> newCorrectTokens = new ArrayList<>();
            while (index < correctTokens.size()) {
                while (index < correctTokens.size() && !correctTokens.get(index).equals("\"") && !correctTokens.get(index).equals("'")) {
                    newCorrectTokens.add(correctTokens.get(index));
                    index++;
                }
                if (index < correctTokens.size()) {
                    StringBuilder constant = new StringBuilder();
                    String constSeparator = correctTokens.get(index);
                    constant.append(constSeparator);
                    index++;
                    while (index < correctTokens.size() && !correctTokens.get(index).equals(constSeparator)) {
                        constant.append(correctTokens.get(index));
                        index++;
                    }
                    if (index < correctTokens.size() && correctTokens.get(index).equals(constSeparator)) {
                        constant.append(constSeparator);
                    }
                    newCorrectTokens.add(constant.toString());
                }
                index++;
            }
            newCorrectTokens.remove(" ");
            return newCorrectTokens;
        }
        correctTokens.remove(" ");
        return correctTokens;
    }

    private boolean isValidIdentifier(final String identifier) {
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher matcher = pattern.matcher(identifier);
        return matcher.find();
    }

    private boolean isValidConstant(final String constant) {
        boolean validConstant = false;
        for (final String regex : this.constantRegex) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(constant);
            if (matcher.find()) {
                validConstant = true;
            }
        }
        return validConstant;
    }

    private boolean isValidNumerical(final String constant) {
        Pattern pattern = Pattern.compile(numericalRegex);
        Matcher matcher = pattern.matcher(constant);
        return matcher.find();
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
                tokens = fixNumericalConstants(tokens);
                System.out.println(lineNumber + ": " + tokens);
                errors.append(classify(tokens, lineNumber));
                codify(tokens);
                lineNumber += 1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.errors = errors + "\n";
        if (errors.toString().isEmpty()) {
            System.out.println("Correct!");
        } else {
            System.out.println(errors);
        }
        writeToFile();
    }

    private List<String> fixNumericalConstants(final List<String> tokens) {
        final List<String> newTokens = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("+") || tokens.get(i).equals("-")) {
                String before = i - 1 >= 0 ? tokens.get(i - 1) : null;
                String after = i + 1 < tokens.size() ? tokens.get(i + 1) : null;
                if (isValidNumerical(after) && ("=".equals(before) || "<".equals(before) || ">".equals(before) || "(".equals(before) || before == null)) {
                    newTokens.add(tokens.get(i) + after);
                    i++;
                } else {
                    newTokens.add(tokens.get(i));
                }
            } else {
                newTokens.add(tokens.get(i));
            }
        }
        return newTokens;
    }

    private boolean isSymbol(final String token) {
        return (isValidIdentifier(token) || isValidConstant(token)) && !reservedWords.contains(token) && !separators.contains(token) && !operators.contains(token);
    }

    private void writeToFile() {
        writePIF();
        writeST();
    }

    private void writePIF() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(pifPath))) {
            bw.write(errors);
            bw.write(pif.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeST() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(stPath))) {
            bw.write("Symbol table is implemented using hash table and linked list for collision control.\n\n");
            bw.write(errors);
            bw.write(symbolTable.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void codify(List<String> tokens) {
        for (final String token : tokens) {
            Pair<Integer, Integer> positions = isSymbol(token) ? symbolTable.insert(token) : new Pair<>(-1, -1);
            if (isValidIdentifier(token)) {
                pif.add("id", positions);
            } else {
                if (isValidConstant(token)) {
                    pif.add("const", positions);
                } else {
                    pif.add(token, positions);
                }
            }
        }
    }
}
