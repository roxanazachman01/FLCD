package ro.flcd.domain;

import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {

    @Test
    public void firstTestSeminarGrammar() {
        Parser parser = new Parser("in/g3.txt");
        var first = parser.getFirst();
        for (var term : parser.getGrammar().getTerminals()) {
            assertThat(first.get(term)).isEqualTo(Set.of(term));
        }

        assertThat(first.get(new Nonterminal("S"))).isEqualTo(Set.of(new Terminal("("),new Terminal("a")));
        assertThat(first.get(new Nonterminal("A"))).isEqualTo(Set.of(new Terminal("+"),new Epsilon()));
        assertThat(first.get(new Nonterminal("B"))).isEqualTo(Set.of(new Terminal("("),new Terminal("a")));
        assertThat(first.get(new Nonterminal("C"))).isEqualTo(Set.of(new Terminal("*"),new Epsilon()));
        assertThat(first.get(new Nonterminal("D"))).isEqualTo(Set.of(new Terminal("("),new Terminal("a")));
    }
    @Test
    public void followTestSeminarGrammar() {
        Parser parser = new Parser("in/g3.txt");
        var follow = parser.getFollow();

        assertThat(follow.get(new Nonterminal("S"))).isEqualTo(Set.of(new Terminal(")"),new Epsilon()));
        assertThat(follow.get(new Nonterminal("A"))).isEqualTo(Set.of(new Terminal(")"),new Epsilon()));
        assertThat(follow.get(new Nonterminal("B"))).isEqualTo(Set.of(new Terminal(")"),new Terminal("+"),new Epsilon()));
        assertThat(follow.get(new Nonterminal("C"))).isEqualTo(Set.of(new Terminal(")"),new Terminal("+"),new Epsilon()));
        assertThat(follow.get(new Nonterminal("D"))).isEqualTo(Set.of(new Terminal(")"),new Terminal("+"),new Terminal("*"),new Epsilon()));
    }

    @Test
    public void firstTestEpsilon() {
        Parser parser = new Parser("in/g4.txt");
        var first = parser.getFirst();
        for (var term : parser.getGrammar().getTerminals()) {
            assertThat(first.get(term)).isEqualTo(Set.of(term));
        }

        assertThat(first.get(new Nonterminal("S"))).isEqualTo(Set.of(new Terminal("*"),new Terminal("+")));
        assertThat(first.get(new Nonterminal("A"))).isEqualTo(Set.of(new Terminal("*"),new Epsilon()));
        assertThat(first.get(new Nonterminal("B"))).isEqualTo(Set.of(new Terminal("+")));
        assertThat(first.get(new Nonterminal("C"))).isEqualTo(Set.of(new Terminal("-"),new Epsilon()));
    }
    @Test
    public void followTestEpsilon() {
        Parser parser = new Parser("in/g4.txt");
        var follow = parser.getFollow();

        assertThat(follow.get(new Nonterminal("S"))).isEqualTo(Set.of(new Epsilon()));
        assertThat(follow.get(new Nonterminal("A"))).isEqualTo(Set.of(new Terminal("+")));
        assertThat(follow.get(new Nonterminal("B"))).isEqualTo(Set.of(new Terminal("-"),new Epsilon()));
        assertThat(follow.get(new Nonterminal("C"))).isEqualTo(Set.of(new Epsilon()));
    }
}