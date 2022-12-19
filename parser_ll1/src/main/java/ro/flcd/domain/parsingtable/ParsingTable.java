package ro.flcd.domain.parsingtable;

import org.apache.commons.lang3.tuple.Pair;
import ro.flcd.Parser;
import ro.flcd.domain.grammar.Epsilon;
import ro.flcd.domain.grammar.Grammar;
import ro.flcd.domain.grammar.TermOrNonTerm;
import ro.flcd.domain.grammar.Terminal;

import java.io.*;
import java.util.*;

public class ParsingTable {
    private final Parser parser;

    private final Grammar grammar;

    private final Map<Pair<TermOrNonTerm, TermOrNonTerm>, ParsingTableValue> table = new HashMap<>();

    private final List<Pair<TermOrNonTerm, Pair<Integer, Integer>>> parseTable = new ArrayList<>();

    public ParsingTable(final Parser parser) {
        this.parser = parser;
        this.grammar = parser.getGrammar();
        createAndInitTable();
        fillParsingTable();
    }

    private void fillParsingTable() {
        for (var nonterm : grammar.getNonterminals()) {
            for (var prod : grammar.getProductionsOfNonterminal(nonterm)) {
                var pair = parser.getFirstAndIndexForRhs(prod);
                Set<Terminal> first = pair.getLeft();
                Integer index = pair.getRight();
                for (var term : first) {
                    if (!table.get(Pair.of(nonterm, term)).equals(SpecialTableValue.ERR)) {
                        System.out.println("Old value: " + Pair.of(nonterm, term) + " -> " + table.get(Pair.of(nonterm, term)));
                        System.out.println("New value: " + Pair.of(nonterm, term) + " -> " + new NormalTableValue(prod.getRightHS(), index));
                        throw new RuntimeException("Conflict: " + nonterm + " " + term);
                    }
                    if (!term.equals(new Epsilon())) {
                        table.put(Pair.of(nonterm, term), new NormalTableValue(prod.getRightHS(), index));
                    }
                }
                if (first.contains(new Epsilon())) {
                    var follow = parser.getFollow().get(nonterm);
                    for (var term : follow) {
                        if (!term.isEpsilon()) {
                            if (!table.get(Pair.of(nonterm, term)).equals(SpecialTableValue.ERR)) {
                                throw new RuntimeException("Conflict: " + nonterm + " " + term);
                            }
                            table.put(Pair.of(nonterm, term), new NormalTableValue(prod.getRightHS(), index));
                        } else {
                            table.put(Pair.of(nonterm, new DollarSign()), new NormalTableValue(prod.getRightHS(), index));
                        }
                    }
                }
            }
        }
    }

    public void printParsingTable() {
        List<TermOrNonTerm> colHeader = new ArrayList<>(grammar.getTerminals());
        colHeader.add(new DollarSign());
        colHeader.remove(new Epsilon());

        List<TermOrNonTerm> rowHeader = new ArrayList<>(grammar.getNonterminals());
        rowHeader.addAll(colHeader);

//        System.out.println(colHeader);
//        System.out.println(rowHeader);

        System.out.println(parser.getFirst());
        System.out.println(parser.getFollow());
        for (var rowH : rowHeader) {
            for (var colH : colHeader) {
                if (!table.get(Pair.of(rowH, colH)).equals(SpecialTableValue.ERR)) {
                    System.out.println(rowH + "," + colH + ": " + table.get(Pair.of(rowH, colH)));
                }
            }
        }
    }

    private void createAndInitTable() {
        for (var nonterm : grammar.getNonterminals()) {
            for (var term : grammar.getTerminals()) {
                if (!term.isEpsilon()) {
                    table.put(Pair.of(nonterm, term), SpecialTableValue.ERR);
                }
            }
            table.put(Pair.of(nonterm, new DollarSign()), SpecialTableValue.ERR);
        }
        System.out.println();
        for (var term1 : grammar.getTerminals()) {
            if (!term1.isEpsilon()) {
                for (var term2 : grammar.getTerminals()) {
                    if (!term2.isEpsilon()) {
                        table.put(Pair.of(term1, term2), SpecialTableValue.ERR);
                        //M(a,a)=POP
                        if (term1.equals(term2)) {
                            table.put(Pair.of(term1, term2), SpecialTableValue.POP);
                        } else { //M(x,a)=ERR
                            table.put(Pair.of(term1, term2), SpecialTableValue.ERR);
                        }
                    }
                }
                table.put(Pair.of(term1, new DollarSign()), SpecialTableValue.ERR);
            }
        }
        for (var term : grammar.getTerminals()) {
            if (!term.isEpsilon()) {
                table.put(Pair.of(new DollarSign(), term), SpecialTableValue.ERR);
            }
        }
        //M($,$)=ACC
        table.put(Pair.of(new DollarSign(), new DollarSign()), SpecialTableValue.ACC);
    }

    public boolean parseSequenceFromFile(final String path) {
        List<Terminal> sequence = new ArrayList<>();
        var filePath = Grammar.class.getClassLoader().getResource(path).getPath();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            var parts = br.readLine().split(",");
            for (var part : parts) {
                var term = new Terminal(part);
                if (grammar.getTerminals().contains(term)) {
                    sequence.add(term);
                } else {
                    throw new RuntimeException("Invalid sequence from file. Element " + part + " is not a terminal in the grammar " + grammar.getFilePath() + ".");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parseSequence(sequence);
    }

    public boolean parseSequenceFromPif(String path) {
        List<Terminal> sequence = new ArrayList<>();
        var filePath = Grammar.class.getClassLoader().getResource(path).getPath();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    var parts = line.split(",");
                    var tokenPart = parts[0].split(" ")[1];
                    tokenPart = tokenPart.substring(1, tokenPart.length() - 1);
                    var term = new Terminal(tokenPart);
                    if (grammar.getTerminals().contains(term)) {
                        sequence.add(term);
                    } else {
                        throw new RuntimeException("Invalid sequence from file. Element " + tokenPart + " is not a terminal in the grammar " + grammar.getFilePath() + ".");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(sequence);
        return parseSequence(sequence);
    }

    public boolean parseSequence(List<Terminal> sequence) {
        Configuration configuration = Configuration.getInitialConfig(table, sequence, grammar.getStartSymbol());
        boolean run = true, error = false;
//        List<Configuration> configurations = new ArrayList<>();
        var filePath = Grammar.class.getClassLoader().getResource("out/configs.out").getPath();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            while (run) {
//                configurations.add(configuration.deepCopy());
                bw.write(configuration.deepCopy().toString());
                bw.write("\n");
                if (configuration.shouldPush()) {
                    configuration.push();
                } else {
                    if (configuration.shouldPop()) {
                        configuration.pop();
                    } else {
                        if (!configuration.isAccepted()) {
                            error = true;
                            System.out.println(configuration);
                        }
                        run = false;
                    }
                }
            }
            bw.write(configuration.deepCopy().toString());
            bw.write("\n");

//        configurations.add(configuration.deepCopy());
//        writeConfigsToFile(configurations);
            if (error) {
                System.out.println("Sequence is not accepted.");
                bw.write("ERROR!!! --- Sequence is not accepted.");
                bw.write("\n");
                bw.flush();
                return false;
            } else {
                System.out.println("Sequence is accepted.");
                constructParseTree(configuration.getProductionsIndexes());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void writeConfigToFile(Configuration configuration) {
        var filePath = Grammar.class.getClassLoader().getResource("out/configs.out").getPath();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(configuration.toString());
            bw.write("\n");
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeConfigsToFile(List<Configuration> configurations) {
        var filePath = Grammar.class.getClassLoader().getResource("out/configs.out").getPath();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (var c : configurations) {
                bw.write(c.toString());
                bw.write("\n");
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void constructParseTree(List<Integer> productionsIndexes) {
        List<Integer> qq = new ArrayList<>();
        qq.add(0);
        parseTable.add(Pair.of(grammar.getStartSymbol(), Pair.of(-1, -1)));
        for (var prodIndex : productionsIndexes) {
            var prod = grammar.getProductions().get(prodIndex);
            var parent = qq.remove(0);

            var leftNode = prod.getRightHS().get(0);
            parseTable.add(Pair.of(leftNode, Pair.of(parent, -1)));
            List<Integer> siblings = new ArrayList<>();
            if (!leftNode.isTerminal()) {
                siblings.add(parseTable.size() - 1);
            }
            for (int index = 1; index < prod.getRightHS().size(); index++) {
                var currentNode = prod.getRightHS().get(index);
                parseTable.add(Pair.of(currentNode, Pair.of(parent, parseTable.size() - 1)));
                if (!currentNode.isTerminal()) {
                    siblings.add(parseTable.size() - 1);
                }
            }
            qq.addAll(0, siblings);
        }
//        for (int index = 0; index < parseTable.size(); index++) {
//            System.out.println(index + " --- info: " + parseTable.get(index).getKey() + "; parent: " + parseTable.get(index).getValue().getKey() + " leftSibling: " + parseTable.get(index).getValue().getValue());
//        }
        writeParseTreeToFile(parseTable);
    }

    private void writeParseTreeToFile(List<Pair<TermOrNonTerm, Pair<Integer, Integer>>> parseTable) {
        var filePath = Grammar.class.getClassLoader().getResource("out/parseTree.out").getPath();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (int index = 0; index < parseTable.size(); index++) {
                bw.write(index + " --- info: " + parseTable.get(index).getKey() + "; parent: " + parseTable.get(index).getValue().getKey() + " leftSibling: " + parseTable.get(index).getValue().getValue());
                bw.write("\n");
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
