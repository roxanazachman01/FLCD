package ro.flcd;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FiniteAutomata { // validare
    /*
     * Q - states
     * E - alphabet
     * D - transitions
     * q0 - initial state
     * F - final state
     * */
    private String faPath;
    private boolean isDFA;
    private Set<String> states;
    private Set<String> alphabet;
    private Set<Transition> transitions = new HashSet<>();
    private Map<Pair<String, String>, List<String>> transitionsMap = new HashMap<>();
    private String initialState;
    private Set<String> finalStates;

    public FiniteAutomata(final String faPath) {
        this.loadFromFile(faPath);
    }

    public static boolean verifyValidIntegerConstant(final String integer) {
        FiniteAutomata integerFA = new FiniteAutomata("in/integerFA.in");
        return integerFA.verifySequence(integer);
    }

    public static boolean verifyValidStringConstant(final String string) {
        FiniteAutomata stringFA = new FiniteAutomata("in/stringFA.in");
        return stringFA.verifySequence(string);
    }

    public static boolean verifyValidCharConstant(final String charToken) {
        FiniteAutomata charFA = new FiniteAutomata("in/charFA.in");
        return charFA.verifySequence(charToken);
    }

    public static boolean verifyValidIdentifier(final String identifier) {
        FiniteAutomata identifierFA = new FiniteAutomata("in/identifierFA.in");
        return identifierFA.verifySequence(identifier);
    }

    public static boolean verifyValidConstant(final String token) {
        return verifyValidIntegerConstant(token) || verifyValidStringConstant(token) || verifyValidCharConstant(token);
    }

    public void loadFromFile(final String faPath) {
        this.faPath = FiniteAutomata.class.getClassLoader().getResource(faPath).getPath();
        this.loadFA();
        this.validateFA();
        this.isDFA = checkIfDFA();
    }

    private void validateFA() {
        final StringBuilder errors = new StringBuilder();
        final StringBuilder warnings = new StringBuilder();
        if (states.isEmpty()) {
            errors.append("Empty set of states!\n");
        }
        if (alphabet.isEmpty()) {
            errors.append("Empty alphabet!\n");
        }
        // states from transitions
        var statesFromTransitions = new HashSet<String>();
        var acceptedFromTransitions = new HashSet<String>();
        var finalStatesFromTransitions = new HashSet<String>();
        var leftHSTransitions = new HashSet<String>();
        for (Transition transition : transitions) {
            statesFromTransitions.add(transition.getState());
            statesFromTransitions.add(transition.getResult());
            acceptedFromTransitions.add(transition.getAccepted());
            finalStatesFromTransitions.add(transition.getResult());
            leftHSTransitions.add(transition.getState());
        }
        if (!states.containsAll(statesFromTransitions)) {
            errors.append("Transitions contain states not declared in set of states!\n");
        }
        if (!statesFromTransitions.containsAll(states)) {
            warnings.append("Set of states contains states not used in transitions!\n");
        }
        if (!alphabet.containsAll(acceptedFromTransitions)) {

            System.out.println(alphabet);
            System.out.println(acceptedFromTransitions);
            errors.append("Transitions contain accepted elems not declared in alphabet!\n");
        }
        if (!acceptedFromTransitions.containsAll(alphabet)) {
            warnings.append("Alphabet contains elements not used in transitions!\n");
        }
        if (!finalStatesFromTransitions.containsAll(finalStates)) {
            errors.append("Not all states from set of final states are actually final in transitions!\n");
        }
        if (!leftHSTransitions.contains(initialState)) {
            errors.append("Initial state is not actually initial in transitions!\n");
        }
        if (!errors.isEmpty()) {
            if (!warnings.isEmpty()) {
                errors.append("\nWarnings: \n" + warnings + "\n");
            }
            throw new RuntimeException("Errors: \n" + errors + "\n");
        }
        if (!warnings.isEmpty()) {
            System.out.println("Warnings(redundancy): \n" + warnings + "\n");
        }
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    public String getInitialState() {
        return initialState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public boolean isDFA() {
        return isDFA;
    }

    private void loadFA() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.faPath))) {
            states = Arrays.stream(br.readLine().trim().split(" ")).collect(Collectors.toSet());
            alphabet = Arrays.stream(br.readLine().trim().split(",")).collect(Collectors.toSet());
            int transitionNumber = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < transitionNumber; i++) {
                var parts = Arrays.stream(br.readLine().trim().split(" ")).toList();
                var state = parts.get(0);
                var result = parts.get(parts.size() - 1);
                var allAccepted = new ArrayList<String>();
                for (int j = 1; j < parts.size() - 1; j++) {
                    allAccepted.addAll(Arrays.stream(parts.get(j).split(",")).toList());
                }
                if (parts.size() > 3) {
                    allAccepted.add(" ");
                    allAccepted.remove("");
                }
                for (String accepted : allAccepted) {
                    transitions.add(new Transition(state, accepted, result));
                    var key = new Pair<>(state, accepted);
                    if (!transitionsMap.containsKey(key)) {
                        transitionsMap.put(key, new ArrayList<>());
                    }
                    transitionsMap.get(key).add(result);
                }
            }
            initialState = br.readLine().trim();
            finalStates = Arrays.stream(br.readLine().trim().split(" ")).collect(Collectors.toSet());
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

//        for (int i = 0; i < transitions.size() - 1; i++) {
//            for (int j = i + 1; j < transitions.size(); j++) {
//                final Transition t1 = transitions.get(i);
//                final Transition t2 = transitions.get(j);
//                if (Transition.haveSameState(t1, t2) && Transition.haveSameAccepted(t1, t2) && !Transition.haveSameResult(t1, t2)) {
//                    return false;
//                }
//            }
//        }
//        return true;

        for (var results : transitionsMap.values()) {
            if (results.size() > 1) {
                return false;
            }
        }
        return true;
    }
}
