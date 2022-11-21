package ro.flcd.domain;

public class Epsilon extends Terminal {
    public Epsilon(String value) {
        super(value);
    }

    @Override
    public boolean isEpsilon() {
        return true;
    }
}
