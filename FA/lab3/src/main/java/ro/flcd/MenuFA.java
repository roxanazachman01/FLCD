package ro.flcd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MenuFA {
    private final Map<Integer, Consumer<Void>> commands = new HashMap<>();
    private final FiniteAutomata finiteAutomata;

    public MenuFA(final FiniteAutomata finiteAutomata) {
        this.finiteAutomata = finiteAutomata;
        commands.put(1, c -> showStates());
        commands.put(2, c -> showAlphabet());
        commands.put(3, c -> showTransitions());
        commands.put(4, c -> showInitialState());
        commands.put(5, c -> showFinalStates());
        commands.put(6, c -> showIsDFA());
        commands.put(7, c -> checkSequenceAccepted());
        commands.put(8, c -> checkSequencesAccepted());
    }


    private void showMenu() {
        System.out.println("1. Show set of states.");
        System.out.println("2. Show alphabet.");
        System.out.println("3. Show transitions.");
        System.out.println("4. Show initial state.");
        System.out.println("5. Show set of final states.");
        System.out.println("6. Is DFA?");
        System.out.println("7. Check if sequence is accepted (console).");
        System.out.println("8. Check if sequences are accepted (file).");
        System.out.println("0. Exit");
    }

    private void showStates() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Set of states:\n").append("Q = {");
        String states = finiteAutomata.getStates().stream().collect(Collectors.joining(", "));
        sb.append(states).append("};");
        System.out.println(sb);
    }

    private void showAlphabet() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Alphabet:\n").append("E = {");
        String states = finiteAutomata.getAlphabet().stream().collect(Collectors.joining(", "));
        sb.append(states).append("};");
        System.out.println(sb);
    }

    private void showTransitions() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Transitions:\n").append("D = {");
        String states = finiteAutomata.getTransitions().stream().map(Transition::toString).collect(Collectors.joining(", "));
        sb.append(states).append("};");
        System.out.println(sb);
    }

    private void showInitialState() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Initial state:\n").append("q0 = ");
        String states = finiteAutomata.getInitialState();
        sb.append(states).append(";");
        System.out.println(sb);
    }

    private void showFinalStates() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Set of final states:\n").append("F = {");
        String states = finiteAutomata.getFinalStates().stream().collect(Collectors.joining(", "));
        sb.append(states).append("};");
        System.out.println(sb);
    }

    private void showIsDFA() {
        if (finiteAutomata.isDFA()) {
            System.out.println("The finite automata is DFA.");
        } else {
            System.out.println("The finite automata is NOT DFA.");
        }
    }

    private void checkSequenceAccepted() {
        final Scanner scanner = new Scanner(System.in);
        System.out.print("Enter sequence: ");
        final String sequence = scanner.next();
        final boolean accepted = finiteAutomata.verifySequence(sequence);
        if (accepted) {
            System.out.println("The sequence " + sequence + " is accepted by the FA.");
        } else {
            System.out.println("The sequence " + sequence + " is NOT accepted by the FA.");
        }
    }

    private void checkSequencesAccepted() {
        final String sequencesPath = ro.flcd.MenuFA.class.getClassLoader().getResource("in/sequences.in").getPath();
        try (BufferedReader br = new BufferedReader(new FileReader(sequencesPath))) {
            String sequence;
            while ((sequence = br.readLine()) != null) {
                final boolean accepted = finiteAutomata.verifySequence(sequence);
                if (accepted) {
                    System.out.println("The sequence " + sequence + " is accepted by the FA.");
                } else {
                    System.out.println("The sequence " + sequence + " is NOT accepted by the FA.");
                }
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            System.out.print("\t> ");
            try {
                int choice = scanner.nextInt();
                if (commands.containsKey(choice)) {
                    commands.get(choice).accept(null);
                    System.out.println();
                } else {
                    if (choice == 0) {
                        System.out.println("Bye.");
                        return;
                    } else {
                        System.out.println("Invalid choice!\n");
                    }
                }
            } catch (final InputMismatchException e) {
                System.out.println("Invalid choice!\n");
                scanner.next();
            }
        }
    }
}
