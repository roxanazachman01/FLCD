package ro.flcd;

import java.util.Objects;

public class Transition {
    private final String state;
    private final String accepted;
    private final String result;

    public Transition(String state, String accepted, String result) {
        this.state = state;
        this.accepted = accepted;
        this.result = result;
    }

    public static boolean haveSameState(final Transition t1, final Transition t2) {
        return t1.getState().equals(t2.getState());
    }
    public static boolean haveSameAccepted(final Transition t1, final Transition t2) {
        return t1.getAccepted().equals(t2.getAccepted());
    }
    public static boolean haveSameResult(final Transition t1, final Transition t2) {
        return t1.getResult().equals(t2.getResult());
    }
    public String getState() {
        return state;
    }

    public String getAccepted() {
        return accepted;
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transition that)) return false;
        return getState().equals(that.getState()) && getAccepted().equals(that.getAccepted()) && getResult().equals(that.getResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getState(), getAccepted(), getResult());
    }

    @Override
    public String toString() {
        return "d(" + state + ", " + accepted + ") = " + result;
    }
}
