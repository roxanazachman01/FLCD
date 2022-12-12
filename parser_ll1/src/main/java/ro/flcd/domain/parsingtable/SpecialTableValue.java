package ro.flcd.domain.parsingtable;

import ro.flcd.domain.grammar.TermOrNonTerm;

import java.util.List;

public enum SpecialTableValue implements ParsingTableValue {
    POP,
    ACC,
    ERR;

    @Override
    public List<TermOrNonTerm> getRhs() {
        return null;
    }

    @Override
    public Integer getProductionIndex() {
        return null;
    }

    @Override
    public boolean isAcc() {
        return this.equals(SpecialTableValue.ACC);
    }

    @Override
    public boolean isPop() {
        return this.equals(SpecialTableValue.POP);
    }

    @Override
    public boolean isErr() {
        return this.equals(SpecialTableValue.ERR);
    }
}
