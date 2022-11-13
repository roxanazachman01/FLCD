package ro.flcd;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FiniteAutomataTest {
    private static FiniteAutomata finiteAutomata;

    @BeforeAll
    static void beforeAll() {
        finiteAutomata = new FiniteAutomata("in/integerFA.in");
    }

    @Test
    void getStates() {
        Set<String> expected = new HashSet<>(List.of("q0", "q1", "qf1", "qf2"));
        assertThat(finiteAutomata.getStates()).isEqualTo(expected);
    }

    @Test
    void getAlphabet() {
        Set<String> expected = new HashSet<>(List.of("+", "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        assertThat(finiteAutomata.getAlphabet()).isEqualTo(expected);
    }

    @Test
    void getTransitions() {
        List<Transition> expected = new ArrayList<>();
        expected.add(new Transition("q0", "0", "qf1"));
        expected.add(new Transition("q0", "1", "qf2"));
        expected.add(new Transition("q0", "2", "qf2"));
        expected.add(new Transition("q0", "3", "qf2"));
        expected.add(new Transition("q0", "4", "qf2"));
        expected.add(new Transition("q0", "5", "qf2"));
        expected.add(new Transition("q0", "6", "qf2"));
        expected.add(new Transition("q0", "7", "qf2"));
        expected.add(new Transition("q0", "8", "qf2"));
        expected.add(new Transition("q0", "9", "qf2"));
        expected.add(new Transition("q0", "-", "q1"));
        expected.add(new Transition("q0", "+", "q1"));
        expected.add(new Transition("q1", "1", "qf2"));
        expected.add(new Transition("q1", "2", "qf2"));
        expected.add(new Transition("q1", "3", "qf2"));
        expected.add(new Transition("q1", "4", "qf2"));
        expected.add(new Transition("q1", "5", "qf2"));
        expected.add(new Transition("q1", "6", "qf2"));
        expected.add(new Transition("q1", "7", "qf2"));
        expected.add(new Transition("q1", "8", "qf2"));
        expected.add(new Transition("q1", "9", "qf2"));
        expected.add(new Transition("qf2", "0", "qf2"));
        expected.add(new Transition("qf2", "1", "qf2"));
        expected.add(new Transition("qf2", "2", "qf2"));
        expected.add(new Transition("qf2", "3", "qf2"));
        expected.add(new Transition("qf2", "4", "qf2"));
        expected.add(new Transition("qf2", "5", "qf2"));
        expected.add(new Transition("qf2", "6", "qf2"));
        expected.add(new Transition("qf2", "7", "qf2"));
        expected.add(new Transition("qf2", "8", "qf2"));
        expected.add(new Transition("qf2", "9", "qf2"));
        assertThat(finiteAutomata.getTransitions()).isEqualTo(expected);
    }

    @Test
    void getInitialState() {
        String expected = "q0";
        assertThat(finiteAutomata.getInitialState()).isEqualTo(expected);
    }

    @Test
    void getFinalStates() {
        Set<String> expected = new HashSet<>(List.of("qf1", "qf2"));
        assertThat(finiteAutomata.getFinalStates()).isEqualTo(expected);
    }

    @Test
    void isDFA() {
        assertThat(finiteAutomata.isDFA()).isTrue();
    }

    @Test
    void verifySequence() {
        assertThat(finiteAutomata.verifySequence("0")).isTrue();
        assertThat(finiteAutomata.verifySequence("1")).isTrue();
        assertThat(finiteAutomata.verifySequence("+0")).isFalse();
        assertThat(finiteAutomata.verifySequence("-0")).isFalse();
        assertThat(finiteAutomata.verifySequence("+1")).isTrue();
        assertThat(finiteAutomata.verifySequence("+123")).isTrue();
        assertThat(finiteAutomata.verifySequence("-2")).isTrue();
        assertThat(finiteAutomata.verifySequence("-234")).isTrue();
        assertThat(finiteAutomata.verifySequence("0123")).isFalse();
        assertThat(finiteAutomata.verifySequence("+012")).isFalse();
        assertThat(finiteAutomata.verifySequence("-012")).isFalse();
        assertThat(finiteAutomata.verifySequence("1+2")).isFalse();
        assertThat(finiteAutomata.verifySequence("230423")).isTrue();
        assertThat(finiteAutomata.verifySequence("234230")).isTrue();
    }
}