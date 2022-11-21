package ro.flcd.domain;

public class Terminal implements TermOrNonTerm{
    private final String value;

    public Terminal(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return this.value;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isEpsilon() {
        return false;
    }
}
