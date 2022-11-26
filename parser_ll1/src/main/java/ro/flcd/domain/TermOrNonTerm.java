package ro.flcd.domain;

public interface TermOrNonTerm {
    String value();

    boolean isTerminal();

    boolean isEpsilon();
}
