package ro.flcd.domain.grammar;

public interface TermOrNonTerm {
    String value();

    boolean isTerminal();

    boolean isEpsilon();

    boolean isDollar();
}
