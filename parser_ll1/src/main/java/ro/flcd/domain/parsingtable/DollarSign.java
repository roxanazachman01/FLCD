package ro.flcd.domain.parsingtable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ro.flcd.domain.grammar.TermOrNonTerm;

@Getter
@EqualsAndHashCode
public class DollarSign implements TermOrNonTerm {
    private final String value;

    public DollarSign() {
        this.value = "$";
    }

    @Override
    public String value() {
        return this.value;
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
    public boolean isDollar() {
        return true;
    }

    @Override
    public String toString() {
        return value;
    }
}
