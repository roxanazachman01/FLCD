package ro.flcd.domain;

public class Nonterminal implements TermOrNonTerm {
    private final String value;

    public Nonterminal(String value) {
        this.value = value;
    }

    @Override
    public String get() {
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

}
