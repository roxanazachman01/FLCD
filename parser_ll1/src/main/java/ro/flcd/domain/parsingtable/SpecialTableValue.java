package ro.flcd.domain.parsingtable;

public enum SpecialTableValue implements ParsingTableValue{
    POP,
    ACC,
    ERR;

    @Override
    public String value() {
        return this.toString();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isEpsilon() {
        return false;
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

    @Override
    public boolean isDollar() {
        return false;
    }
}
