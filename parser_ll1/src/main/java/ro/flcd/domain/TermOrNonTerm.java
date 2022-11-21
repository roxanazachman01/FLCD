package ro.flcd.domain;

import ro.flcd.domain.Type;

public interface TermOrNonTerm {
    String get();

    boolean isTerminal();
    boolean isEpsilon();
}
