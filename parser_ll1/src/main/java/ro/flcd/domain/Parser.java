package ro.flcd.domain;

import lombok.Getter;
import ro.flcd.Grammar;

import java.util.*;

@Getter
public class Parser {
    private final Grammar grammar = new Grammar("in/g3.txt");
    private Map<TermOrNonTerm, Set<Terminal>> follow = new HashMap<>();
    private final List<Map<TermOrNonTerm, Set<Terminal>>> firstTable = new ArrayList<>();
    private final List<Map<TermOrNonTerm, Set<Terminal>>> followTable = new ArrayList<>();
    private Map<TermOrNonTerm, Set<Terminal>> first = new HashMap<>();

    public Parser() {
        initFirstTable();
        initFollowTable();
    }

    public void addInFirstTableForNonterminal(final Nonterminal nonterm, Terminal term) {
        firstTable.get(firstTable.size() - 1).get(nonterm).add(term);
    }

    private void initNextColumn(List<Map<TermOrNonTerm,Set<Terminal>>> table) {
        table.add(new HashMap<>());
        for (var nonterm : grammar.getNonterminals()) {
            table.get(table.size() - 1).put(nonterm, new HashSet<>());
            table.get(table.size() - 1).get(nonterm).addAll(table.get(table.size() - 2).get(nonterm));
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
//                    firstTable.get(0).get(nonterm).add((Terminal) firstInProd);
                    addInFirstTableForNonterminal(nonterm, (Terminal) firstInProd);
                }
            }
        }
    }

    private void initFollowTable() {
        followTable.add(new HashMap<>());
        for (var nonterm : grammar.getNonterminals()) {
                followTable.get(followTable.size() - 1).put(nonterm, new HashSet<>());
        }
        followTable.get(followTable.size()-1).get(grammar.getStartSymbol()).add(new Epsilon());
    }


    public boolean finishedTable(List<Map<TermOrNonTerm,Set<Terminal>>> table) {
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

    private Set<Terminal> getPreviousFirstOfNonterm(Nonterminal nonterm) {
        return firstTable.get(firstTable.size() - 2).get(nonterm);
    }
    private Set<Terminal> getPreviousFollowOfNonterm(Nonterminal nonterm) {
        return followTable.get(followTable.size() - 2).get(nonterm);
    }
    private void printCurrentColumn(List<Map<TermOrNonTerm, Set<Terminal>>> table) {
        System.out.println(table.get(table.size() - 1));
    }

    public void firstAlgorithm() {
        do {
//            firstTable.add(new HashMap<>());
            printCurrentColumn(firstTable);
            initNextColumn(firstTable);
            for (var nonterm : grammar.getNonterminals()) {
                for (var prod : grammar.getProductionsOfNonterminal(nonterm)) {
                    var firstInProd = prod.getRightHS().get(0);
                    if (firstInProd.isTerminal()) {
                        concat(nonterm, (Terminal) firstInProd);
                    } else {
                        concat(nonterm, getPreviousFirstOfNonterm((Nonterminal) firstInProd));
                    }
                }
            }

        } while (!finishedTable(firstTable));
        first = firstTable.get(firstTable.size() - 1);
    }
    private void concatFollow(Nonterminal nonterminal,Nonterminal otherNonterm){
        var terminals = followTable.get(followTable.size()-2).get(otherNonterm);
        followTable.get(followTable.size()-1).get(nonterminal).addAll(terminals);
    }
    public void followAlgorithm() {
        do {
//            firstTable.add(new HashMap<>());
            printCurrentColumn(followTable);
            initNextColumn(followTable);
            for (var nonterm : grammar.getNonterminals()) {
                for (var prod : grammar.getProductionsContainingNonterminalRHS(nonterm)) {
                    var after = grammar.getElementAfterNontermInProd(prod,nonterm);
                    for(var firstOfAfter:first.get(after)){
                        if (after.isEpsilon()){
                            concatFollow(nonterm, (Nonterminal) prod.getLeftHS().get(0));
                        }
                        else {
                            // todo
                        }
                    }
                }
            }

        } while (!finishedTable(followTable));
        follow = followTable.get(followTable.size() - 1);
    }
}
