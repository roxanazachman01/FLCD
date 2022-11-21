package ro.flcd;

import lombok.Getter;
import ro.flcd.domain.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Grammar {
    private String filePath;
    private Set<String> nonterminals = new HashSet<>();
    private Set<String> terminals = new HashSet<>();
    private Set<Production> productions = new HashSet<>();
    private String startSymbol;
    private boolean isCFG;

    public Grammar(final String filePath) {
        this.filePath = Grammar.class.getClassLoader().getResource(filePath).getPath();
        this.loadFromFile(filePath);
    }

    public void loadFromFile(final String filePath) {
        this.loadGrammar();
        this.validateGrammar();
        this.isCFG = checkIfCFG();
    }

    private boolean checkIfCFG() {
        return false;
    }

    private void validateGrammar() {
        for (var terminal : terminals) {
            if (!terminal.equals(Type.EPSILON.toString()) && terminal.length() != 1) {
                throw new RuntimeException("lala1");
            }
            if (!Character.isLowerCase(terminal.charAt(0))) {
                System.out.println(terminal);
                throw new RuntimeException("lala2");
            }
        }
        for (var nonterminal : nonterminals) {
            if (nonterminal.length() != 1) {
                throw new RuntimeException("lala3");
            }
            if (!Character.isUpperCase(nonterminal.charAt(0))) {
                throw new RuntimeException("lala4");
            }
        }
        if (!nonterminals.contains(startSymbol)) {
            throw new RuntimeException("lala5");
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
                var leftHS = parts.get(0);
                var rhsParts = parts.get(1).split("|");

                for (var part : rhsParts) {
                    var termOrNonTermList = Arrays.stream(part.split(",")).toList();
                    var rightHS = new ArrayList<TermOrNonTerm>();
                    for (var termOrNonTerm : termOrNonTermList) {//todo validate nonterminals are NOT lowercase and terminals NOT uppercase
                        if (termOrNonTerm.equals(Type.EPSILON.toString())) {
                            rightHS.add(new Epsilon(termOrNonTerm));
                        } else {
                            if (terminals.contains(termOrNonTerm) && Character.isLowerCase(termOrNonTerm.charAt(0)) && termOrNonTerm.length() == 1) {
                                rightHS.add(new Terminal(termOrNonTerm));
                            } else if (nonterminals.contains(termOrNonTerm) && Character.isUpperCase(termOrNonTerm.charAt(0)) && termOrNonTerm.length() == 1) {
                                rightHS.add(new Nonterminal(termOrNonTerm));
                            }
                        }
                    }
                    productions.add(new Production(leftHS, rightHS));
                }
            }
            startSymbol = br.readLine().trim();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
