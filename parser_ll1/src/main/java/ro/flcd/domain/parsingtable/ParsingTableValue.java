package ro.flcd.domain.parsingtable;

public interface ParsingTableValue {
    String value();

    boolean isTerminal();

    boolean isEpsilon();

    boolean isAcc();

    boolean isPop();

    boolean isErr();

    boolean isDollar();
}
