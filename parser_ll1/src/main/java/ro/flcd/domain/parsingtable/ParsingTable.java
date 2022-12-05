package ro.flcd.domain.parsingtable;

import org.apache.commons.lang3.tuple.Pair;
import ro.flcd.Parser;
import ro.flcd.domain.grammar.Epsilon;
import ro.flcd.domain.grammar.Grammar;
import ro.flcd.domain.grammar.TermOrNonTerm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParsingTable {
    private final Parser parser;

    private final Grammar grammar;

    private final Map<Pair<TermOrNonTerm, TermOrNonTerm>, Set<Pair<ParsingTableValue, Integer>>> table = new HashMap<>();

    public ParsingTable(final Parser parser) {
        this.parser=parser;
        this.grammar = parser.getGrammar();
        createAndInitTable();
        fillParsingTable();
    }

    private void fillParsingTable() {
        for (var nonterm:grammar.getNonterminals()){
            for(var prod:grammar.getProductionsOfNonterminal(nonterm)){
                var pair = parser.getFirstAndIndexForRhs(prod);
                var first = pair.getLeft();
                var index = pair.getRight();
                for(var term:first){
                    if (!term.equals(new Epsilon())){
                        
                    }
                }
            }
        }
    }

    private void createAndInitTable() {
        for (var nonterm : grammar.getNonterminals()) {
            for (var term : grammar.getTerminals()) {
                table.put(Pair.of(nonterm, term), new HashSet<>());
            }
            table.put(Pair.of(nonterm, new DollarSign()), new HashSet<>());
        }
        for (var term1 : grammar.getTerminals()) {
            for (var term2 : grammar.getTerminals()) {
                table.put(Pair.of(term1, term2), new HashSet<>());
                //M(a,a)=POP
                if (term1.equals(term2)) {
                    table.get(Pair.of(term1, term2)).add(Pair.of(SpecialTableValue.POP, 0));
                }
                else{ //M(x,a)=ERR
                    table.get(Pair.of(term1, term2)).add(Pair.of(SpecialTableValue.ERR, 0));
                }
            }
            table.put(Pair.of(term1, new DollarSign()), new HashSet<>());
        }
        //M($,$)=ACC
        table.put(Pair.of(new DollarSign(), new DollarSign()), new HashSet<>());
        table.get(Pair.of(new DollarSign(), new DollarSign())).add(Pair.of(SpecialTableValue.ACC, 0));
    }
}
