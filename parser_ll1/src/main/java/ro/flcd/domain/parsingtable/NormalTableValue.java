package ro.flcd.domain.parsingtable;

import ro.flcd.domain.grammar.TermOrNonTerm;

import java.util.List;
import java.util.stream.Collectors;

public class NormalTableValue implements ParsingTableValue {
    private final List<TermOrNonTerm> rhs;
    private final Integer productionIndex;

    public NormalTableValue(List<TermOrNonTerm> rhs, Integer productionIndex) {
        this.rhs = rhs;
        this.productionIndex = productionIndex;
    }

    @Override
    public List<TermOrNonTerm> getRhs() {
        return rhs;
    }

    @Override
    public Integer getProductionIndex() {
        return productionIndex;
    }

    @Override
    public boolean isAcc() {
        return false;
    }

    @Override
    public boolean isPop() {
        return false;
    }

    @Override
    public boolean isErr() {
        return false;
    }

    @Override
    public String toString() {
        return rhs.stream().map(TermOrNonTerm::toString).collect(Collectors.joining("")) + ", " + productionIndex;
    }
}
