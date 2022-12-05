package ro.flcd.domain.grammar;

import lombok.Getter;

@Getter
public class Epsilon extends Terminal {
    public Epsilon() {
        super("EPSILON");
    }

    @Override
    public boolean isEpsilon() {
        return true;
    }

    @Override
    public String toString() {
        return "EPSILON";
    }
}
