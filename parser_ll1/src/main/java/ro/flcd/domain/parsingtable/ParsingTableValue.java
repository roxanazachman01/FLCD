package ro.flcd.domain.parsingtable;

import ro.flcd.domain.grammar.TermOrNonTerm;

import java.util.List;

public interface ParsingTableValue {
    List<TermOrNonTerm> getRhs();
    Integer getProductionIndex();

    boolean isAcc();

    boolean isPop();

    boolean isErr();
}
