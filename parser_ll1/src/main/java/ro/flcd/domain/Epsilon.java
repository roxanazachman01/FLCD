package ro.flcd.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public class Epsilon extends Terminal {
    public Epsilon(String value) {
        super(value);
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
