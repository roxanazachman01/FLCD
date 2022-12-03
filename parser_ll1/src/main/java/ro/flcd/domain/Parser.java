package ro.flcd.domain;

import lombok.Getter;
import ro.flcd.Grammar;

import java.util.*;

@Getter
public class Parser {
    private final List<Map<TermOrNonTerm, Set<Terminal>>> firstTable = new ArrayList<>();
    private final List<Map<TermOrNonTerm, Set<Terminal>>> followTable = new ArrayList<>();
    private Grammar grammar = new Grammar("in/g3.txt");
    private Map<TermOrNonTerm, Set<Terminal>> follow = new HashMap<>();
    private Map<TermOrNonTerm, Set<Terminal>> first = new HashMap<>();

    public Parser() {
        computeFirstAndFollow();
    }

    public Parser(final Grammar grammar) {
        this.grammar = grammar;
        computeFirstAndFollow();
    }

    public Parser(final String grammarPath) {
        this.grammar = new Grammar(grammarPath);
        computeFirstAndFollow();
    }

    private void computeFirstAndFollow() {
        initFirstTable();
        initFollowTable();
        firstAlgorithm();
        followAlgorithm();
    }

    public void addInFirstTable(final TermOrNonTerm termOrNonTerm, Terminal term) {
        firstTable.get(firstTable.size() - 1).get(termOrNonTerm).add(term);
    }

    private void initNextColumn(List<Map<TermOrNonTerm, Set<Terminal>>> table) {
        table.add(new HashMap<>());
        for (var nonterm : grammar.getNonterminals()) {
            table.get(table.size() - 1).put(nonterm, new HashSet<>());
            table.get(table.size() - 1).get(nonterm).addAll(table.get(table.size() - 2).get(nonterm));
        }
        for (var term : grammar.getTerminals()) {
            table.get(table.size() - 1).put(term, new HashSet<>());
            table.get(table.size() - 1).get(term).addAll(table.get(table.size() - 2).get(term));
        }
    }

    private void initFirstTable() {
        firstTable.add(new HashMap<>());
        for (var nonterm : grammar.getNonterminals()) {
            firstTable.get(firstTable.size() - 1).put(nonterm, new HashSet<>());
            var productionsForNonterm = grammar.getProductionsOfNonterminal(nonterm);
            for (var prod : productionsForNonterm) {
                var firstInProd = prod.getRightHS().get(0);
                if (firstInProd.isTerminal()) {
                    addInFirstTable(nonterm, (Terminal) firstInProd);
                }
            }
        }
        for (var term : grammar.getTerminals()) {
            firstTable.get(firstTable.size() - 1).put(term, new HashSet<>());
            addInFirstTable(term, term);
        }
    }

    private void initFollowTable() {
        followTable.add(new HashMap<>());
        for (var nonterm : grammar.getNonterminals()) {
            followTable.get(followTable.size() - 1).put(nonterm, new HashSet<>());
        }
        for (var term : grammar.getTerminals()) {
            followTable.get(followTable.size() - 1).put(term, new HashSet<>());
        }
        followTable.get(followTable.size() - 1).get(grammar.getStartSymbol()).add(new Epsilon());
    }


    private boolean finishedTable(List<Map<TermOrNonTerm, Set<Terminal>>> table) {
        var previous = table.get(table.size() - 2);
        var last = table.get(table.size() - 1);
        for (var nonterm : grammar.getNonterminals()) {
            if (!previous.get(nonterm).equals(last.get(nonterm))) {
                return false;
            }
        }
        return true;
    }

    private void concat(Nonterminal nonterm, Terminal term) {
        firstTable.get(firstTable.size() - 1).get(nonterm).add(term);
    }

    private void concat(Nonterminal nonterm, Set<Terminal> terms) {
        firstTable.get(firstTable.size() - 1).get(nonterm).addAll(terms);
    }

    private Set<Terminal> getPreviousFirstOfNonterm(TermOrNonTerm termOrNonTerm) {
        return firstTable.get(firstTable.size() - 2).get(termOrNonTerm);
    }

    private Set<Terminal> getCurrentFirstOfNonterm(Nonterminal nonterm) {
        return firstTable.get(firstTable.size() - 1).get(nonterm);
    }

    private Set<Terminal> getPreviousFollowOfNonterm(Nonterminal nonterm) {
        return followTable.get(followTable.size() - 2).get(nonterm);
    }

    private void printCurrentColumn(List<Map<TermOrNonTerm, Set<Terminal>>> table) {
        System.out.println(table.get(table.size() - 1));
    }

    private boolean nontermHasEpsilonRule(Nonterminal nonterm) {
        var prodForNonterm = grammar.getProductionsOfNonterminal(nonterm);
        for (var prod : prodForNonterm) {
            if (prod.getRightHS().contains(new Epsilon())) {
                return true;
            }
        }
        return false;
    }

    private Set<Terminal> handleNonterminalCase(List<TermOrNonTerm> rhs) {
        Set<Terminal> toAdd = new HashSet<>();
        int index;
        for (index = 0; index < rhs.size(); index++) {
            var prevOfNonterm = getPreviousFirstOfNonterm(rhs.get(index));
            toAdd.addAll(prevOfNonterm);
            if (!prevOfNonterm.contains(new Epsilon())) {
                break;
            }
        }
        if (index != rhs.size()) {
            toAdd.remove(new Epsilon());
        }
        return toAdd;
    }

    private boolean previousFirstOfRhsCalculated(Production prod) {
        for (var elem : prod.getRightHS()) {
            if (getPreviousFirstOfNonterm(elem).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void firstAlgorithm() {
        do {
//            printCurrentColumn(firstTable);
            initNextColumn(firstTable);
            for (var nonterm : grammar.getNonterminals()) {
                for (var prod : grammar.getProductionsOfNonterminal(nonterm)) {
                    if (previousFirstOfRhsCalculated(prod)) {
                        var firstInProd = prod.getRightHS().get(0);
                        if (firstInProd.isTerminal()) {
                            concat(nonterm, (Terminal) firstInProd);
                        } else {
                            concat(nonterm, handleNonterminalCase(prod.getRightHS()));
                        }
                    }
                }
            }

        } while (!finishedTable(firstTable));
        first = firstTable.get(firstTable.size() - 1);
    }

    private void concatFollow(Nonterminal nonterminal, Set<Terminal> terminals) {
        followTable.get(followTable.size() - 1).get(nonterminal).addAll(terminals);
    }

    public void followAlgorithm() {
        do {
//            printCurrentColumn(followTable);
            initNextColumn(followTable);
            for (var nonterm : grammar.getNonterminals()) {
                for (var prod : grammar.getProductionsContainingNonterminalRHS(nonterm)) {
                    var lhs = prod.getLeftHS().get(0);
                    var after = grammar.getElementAfterNontermInProd(prod, nonterm);
                    if (after == null) {
                        concatFollow(nonterm, getPreviousFollowOfNonterm((Nonterminal) lhs));
                    } else {
                        var firstOfAfter = new HashSet<>(first.get(after));
                        if (!firstOfAfter.contains(new Epsilon())) {
                            concatFollow(nonterm, firstOfAfter);
                        } else {
                            Set<Terminal> firstOfAfterUnionFollowLhs = new HashSet<>(firstOfAfter);
                            firstOfAfterUnionFollowLhs.remove(new Epsilon());
                            firstOfAfterUnionFollowLhs.addAll(getPreviousFollowOfNonterm((Nonterminal) lhs));
                            concatFollow(nonterm, firstOfAfterUnionFollowLhs);
                        }
                    }
                }
            }

        } while (!finishedTable(followTable));
        follow = followTable.get(followTable.size() - 1);
    }
}
