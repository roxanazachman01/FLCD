package ro.flcd.domain;

import lombok.Getter;

@Getter
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
    public String toString() {
        return value;
    }
}
