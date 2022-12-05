package ro.flcd;

import org.junit.Test;
import ro.flcd.domain.grammar.Grammar;
import ro.flcd.domain.grammar.Nonterminal;
import ro.flcd.domain.grammar.Terminal;

import static org.assertj.core.api.Assertions.assertThat;

public class GrammarTest {
    @Test
    public void testGrammar() {
        final Grammar grammar = new Grammar("in/g2.txt");

        assertThat(grammar.getStartSymbol()).isEqualTo(new Nonterminal("program"));

        assertThat(grammar.getNonterminals()).contains(new Nonterminal("program"));
        assertThat(grammar.getNonterminals()).contains(new Nonterminal("stmt"));
        assertThat(grammar.getNonterminals()).contains(new Nonterminal("term"));
        assertThat(grammar.getNonterminals()).contains(new Nonterminal("term"));

        assertThat(grammar.getTerminals()).contains(new Terminal("{"));
        assertThat(grammar.getTerminals()).contains(new Terminal("while"));
        assertThat(grammar.getTerminals()).contains(new Terminal("if"));

        assertThat(grammar.isCFG()).isTrue();

        assertThat(grammar.getProductionsOfNonterminal(new Nonterminal("condition")).get(0)
                .getRightHS()).contains(new Nonterminal("relation"));
    }
}