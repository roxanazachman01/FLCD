package ro.flcd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiniteAutomata { // validare
    /*
     * Q - states
     * E - alphabet
     * D - transitions
     * q0 - initial state
     * F - final state
     * */
    private final String faPath;
    private final boolean isDFA;
    private List<String> states = new ArrayList<>();
    private List<String> alphabet = new ArrayList<>();
    private List<Transition> transitions = new ArrayList<>();
    private String initialState;
    private List<String> finalStates;

    public FiniteAutomata(final String faPath) {
        this.faPath = FiniteAutomata.class.getClassLoader().getResource(faPath).getPath();
        this.loadFA();
        this.isDFA = checkIfDFA();
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public String getInitialState() {
        return initialState;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public boolean isDFA() {
        return isDFA;
    }

    private void loadFA() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.faPath))) {
            states = Arrays.stream(br.readLine().trim().split(" ")).toList();
            alphabet = Arrays.stream(br.readLine().trim().split(" ")).toList();
            int transitionNumber = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < transitionNumber; i++) {
                var parts = br.readLine().trim().split(" ");
                transitions.add(new Transition(parts[0], parts[1], parts[2]));
            }
            initialState = br.readLine().trim();
            finalStates = Arrays.stream(br.readLine().trim().split(" ")).toList();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifySequence(final String sequence) {
        if (sequence.isEmpty()) {
            return finalStates.contains(initialState);
        }
        return verifySequenceNFA(initialState, sequence);
//        return isDFA ? verifySequenceDFA(initialState, sequence) : verifySequenceNFA(initialState, sequence);
    }

    private boolean verifySequenceNFA(final String state, final String sequence) {
        final List<Boolean> checks = new ArrayList<>();
        verifySequenceNFA(state, sequence, checks);
        return checks.stream().reduce(false, (s, i) -> s || i);
    }

    private boolean verifySequenceNFA(final String state, final String sequence, final List<Boolean> checks) {
        if (sequence.isEmpty() && finalStates.contains(state)) {
            return true;
        }
        for (final Transition transition : transitions) {
            if (!sequence.isEmpty()) {
                if (transition.getState().equals(state) && transition.getAccepted().equals(String.valueOf(sequence.charAt(0)))) {
                    checks.add(verifySequenceNFA(transition.getResult(), sequence.substring(1), checks));
                }
            }
        }
        return false;
    }

    private boolean verifySequenceDFA(final String state, final String sequence) {
        if (sequence.isEmpty() && finalStates.contains(state)) {
            return true;
        }
        if (sequence.isEmpty() && !finalStates.contains(state)) {
            return false;
        }
        for (final Transition transition : transitions) {
            if (transition.getState().equals(state) && transition.getAccepted().equals(String.valueOf(sequence.charAt(0)))) {
                return verifySequenceDFA(transition.getResult(), sequence.substring(1));
            }
        }
        return false;
    }

    private boolean checkIfDFA() {
        // d(q,a)=q' and d(q,a)=q'' not good!
        for (int i = 0; i < transitions.size() - 1; i++) {
            for (int j = i + 1; j < transitions.size(); j++) {
                final Transition t1 = transitions.get(i);
                final Transition t2 = transitions.get(j);
                if (Transition.haveSameState(t1, t2) && Transition.haveSameAccepted(t1, t2) && !Transition.haveSameResult(t1, t2)) {
                    return false;
                }
            }
        }
        return true;
    }
}
