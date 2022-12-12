package ro.flcd.domain.parsingtable;

import org.apache.commons.lang3.tuple.Pair;
import ro.flcd.Parser;
import ro.flcd.domain.grammar.Epsilon;
import ro.flcd.domain.grammar.Grammar;
import ro.flcd.domain.grammar.TermOrNonTerm;
import ro.flcd.domain.grammar.Terminal;
import ro.flcd.domain.parsetree.ParseTree;
import ro.flcd.domain.parsetree.ParseTreeNode;

import java.util.*;

public class ParsingTable {
    private final Parser parser;

    private final Grammar grammar;
    //Pair<ParsingTableValue, Integer> -> PARSINGTABLEVALUE should include rhs of prod!! List<TermOrNonTerm> rightHS;
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
                        System.out.println(table.get(Pair.of(nonterm, term)));
                        System.out.println(new NormalTableValue(prod.getRightHS(), index));
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

    public void parseSequence(List<Terminal> sequence) {
        Configuration configuration = Configuration.getInitialConfig(table, sequence, grammar.getStartSymbol());
        boolean run = true, error = false;
        while (run) {
            System.out.println(configuration);
            if (configuration.shouldPush()) {
                configuration.push();
            } else {
                if (configuration.shouldPop()) {
                    configuration.pop();
                } else {
                    if (!configuration.isAccepted()) {
                        error = true;
                    }
                    run = false;
                }
            }
        }
        System.out.println(configuration);
        if (error) {
            System.out.println("Sequence is not accepted.");
        } else {
            System.out.println("Sequence is accepted.");
            constructParseTree(configuration.getProductionsIndexes());
        }
    }

    private void constructParseTree(List<Integer> productionsIndexes) {
        Queue<ParseTreeNode> qq = new LinkedList<>(); // todo: pair with index in the parseTable to use for parent
        ParseTreeNode root = new ParseTreeNode(grammar.getStartSymbol());
        ParseTree parseTree = new ParseTree(root);
        qq.add(root);
        int tableIndex = 1;
        int parentTableIndex = 0;
        parseTable.add(Pair.of(root.getValue(), Pair.of(-1, -1)));
        for (var prodIndex : productionsIndexes) {
            var prod = grammar.getProductions().get(prodIndex);
            var parent = qq.remove();

            var leftNode = new ParseTreeNode(prod.getRightHS().get(0));
            leftNode.setParent(parent);
            leftNode.setLeftSibling(null);
            System.out.println(parent + " " + leftNode);

            parseTable.add(Pair.of(leftNode.getValue(), Pair.of(parentTableIndex, -1)));

            for (int index = 1; index < prod.getRightHS().size(); index++) {
                var currentNode = new ParseTreeNode(prod.getRightHS().get(index));
                currentNode.setLeftSibling(leftNode);
                currentNode.setParent(parent);
                leftNode.setRightSibling(currentNode);
                qq.add(leftNode);
                leftNode = currentNode;
                System.out.println(parent + " " + leftNode);

                parseTable.add(Pair.of(leftNode.getValue(), Pair.of(parentTableIndex, parseTable.size()-1)));
                tableIndex++;
            }
            qq.add(leftNode);
            parentTableIndex++;
        }
        for (int index = 0; index < parseTable.size(); index++) {
            System.out.println(index + " --- info: " + parseTable.get(index).getKey() + "; parent: " + parseTable.get(index).getValue().getKey() + " leftSibling: " + parseTable.get(index).getValue().getValue());
        }
    }
}
