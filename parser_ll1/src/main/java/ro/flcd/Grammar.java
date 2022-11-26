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
    private Set<String> nonterminals = new HashSet<>();
    private Set<String> terminals = new HashSet<>();
    private String startSymbol;
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
            nonterminals = Arrays.stream(br.readLine().trim().split(",")).collect(Collectors.toSet());
            terminals = Arrays.stream(br.readLine().trim().split(",")).collect(Collectors.toSet());
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
            startSymbol = br.readLine().trim();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<TermOrNonTerm> getTermOrNontermList(List<String> termOrNonTermList) {
        List<TermOrNonTerm> hs = new ArrayList<>();
        for (var termOrNonTerm : termOrNonTermList) {
            if (termOrNonTerm.equals(Type.EPSILON.toString())) {
                hs.add(new Epsilon(termOrNonTerm));
            } else if (terminals.contains(termOrNonTerm)) {
                hs.add(new Terminal(termOrNonTerm));
            } else if (nonterminals.contains(termOrNonTerm)) {
                hs.add(new Nonterminal(termOrNonTerm));
            } else {
                System.out.println(termOrNonTerm);
                throw new RuntimeException("Element in prod rule is neither terminal nor non-terminal.");
            }
        }
        return hs;
    }
    public String getProductions(){
        return productions.stream().map(Production::toString).collect(Collectors.joining("\n"));
    }
}
