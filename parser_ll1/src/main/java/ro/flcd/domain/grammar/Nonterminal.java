package ro.flcd.domain.grammar;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Nonterminal implements TermOrNonTerm {
    private final String value;

    public Nonterminal(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isEpsilon() {
        return false;
    }

    @Override
    public boolean isDollar() {
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}
