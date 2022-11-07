package ro.flcd;

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
    public String toString() {
        return "d(" + state + ", " + accepted + ") = " + result;
    }
}
