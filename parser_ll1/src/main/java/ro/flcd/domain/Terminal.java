package ro.flcd.domain;

import lombok.Getter;

@Getter
public class Terminal implements TermOrNonTerm{
    private final String value;

    public Terminal(String value) {
        this.value = value;
    }

    @Override
    public String value() {
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

    @Override
    public String toString() {
        return value;
    }
}
