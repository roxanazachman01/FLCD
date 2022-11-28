package ro.flcd;

import lombok.Getter;
import ro.flcd.domain.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Grammar {
    private final String filePath;
    private final Set<Production> productions = new HashSet<>();
    private Set<Nonterminal> nonterminals = new HashSet<>();
    private Set<Terminal> terminals = new HashSet<>();
    private Nonterminal startSymbol;
    private boolean isCFG;

    public Grammar(final String filePath) {
        this.filePath = Grammar.class.getClassLoader().getResource(filePath).getPath();
        this.loadFromFile();
    }

    public void loadFromFile() {
        this.loadGrammar();
        this.validateGrammar();
        this.isCFG = checkIfCFG();
    }

    private boolean checkIfCFG() {
        for (var production : productions) {
            if (production.getLeftHS().size() != 1) {
                return false;
            }
        }
        return true;
    }

    private void validateGrammar() {
        if (!nonterminals.contains(startSymbol)) {
            throw new RuntimeException("Starting symbol is not in the set of non-terminals.");
        }
    }

    private void loadGrammar() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            nonterminals = Arrays.stream(br.readLine().trim().split(",")).map(Nonterminal::new).collect(Collectors.toSet());
            terminals = Arrays.stream(br.readLine().trim().split(",")).map(Terminal::new).collect(Collectors.toSet());
            int nrLinesToRead = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < nrLinesToRead; i++) {
                var parts = Arrays.stream(br.readLine().trim().split("#")).toList();
                if (parts.size() != 2) {
                    throw new RuntimeException("Invalid file format.");
                }
                var termOrNonTermList = Arrays.stream(parts.get(0).split(",")).toList();
                var rhsParts = parts.get(1).split("\\|");
                List<TermOrNonTerm> leftHS = getTermOrNontermList(termOrNonTermList);
                for (var part : rhsParts) {
                    termOrNonTermList = Arrays.stream(part.split(",")).toList();
                    var rightHS = getTermOrNontermList(termOrNonTermList);
                    productions.add(new Production(leftHS, rightHS));
                }
            }
            startSymbol = new Nonterminal(br.readLine().trim());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<TermOrNonTerm> getTermOrNontermList(List<String> termOrNonTermList) {
        List<TermOrNonTerm> hs = new ArrayList<>();
        for (var termOrNonTerm : termOrNonTermList) {
            if (termOrNonTerm.equals(Type.EPSILON.toString())) {
                hs.add(new Epsilon());
            } else if (terminals.contains(new Terminal(termOrNonTerm))) {
                hs.add(new Terminal(termOrNonTerm));
            } else if (nonterminals.contains(new Nonterminal(termOrNonTerm))) {
                hs.add(new Nonterminal(termOrNonTerm));
            } else {
                System.out.println(termOrNonTerm);
                throw new RuntimeException("Element in prod rule is  neither terminal nor non-terminal.");
            }
        }
        return hs;
    }
//    public String getProductions(){
//        return productions.stream().map(Production::toString).collect(Collectors.joining("\n"));
//    }

    public List<Production>getProductionsOfNonterminal(final Nonterminal nonterm){
        if (!nonterminals.contains(nonterm)){
            throw new RuntimeException(nonterm+ " is not a nonterminal.");
        }
        List<Production> result = new ArrayList<>();
        for(var prod:productions){
            if (prod.getLeftHS().contains(nonterm)){
                result.add(prod);
            }
        }
        return result;
    }
    public List<Production>getProductionsContainingNonterminalRHS(final Nonterminal nonterm){
        if (!nonterminals.contains(nonterm)){
            throw new RuntimeException(nonterm+ " is not a nonterminal.");
        }
        List<Production> result = new ArrayList<>();
        for(var prod:productions){
            if (prod.getRightHS().contains(nonterm)){
                result.add(prod);
            }
        }
        return result;
    }
    public TermOrNonTerm getElementAfterNontermInProd(final Production prod, final Nonterminal nonterminal){
        var index = prod.getRightHS().indexOf(nonterminal);
        if (index==prod.getRightHS().size()-1){
            return new Epsilon();
        }
        return prod.getRightHS().get(index+1);
    }
}
