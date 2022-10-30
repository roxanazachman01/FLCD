package ro.flcd;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ProgramInternalForm implements Iterable<Pair<String, Pair<Integer, Integer>>> {
    List<Pair<String, Pair<Integer, Integer>>> pif = new ArrayList<>();

    public void add(final String token, final Pair<Integer, Integer> positions) {
        pif.add(new Pair<>(token, positions));
    }

    public int size() {
        return pif.size();
    }

    public Pair<String, Pair<Integer, Integer>> get(int index) {
        return pif.get(index);
    }

    @Override
    public Iterator<Pair<String, Pair<Integer, Integer>>> iterator() {
        return pif.iterator();
    }

    @Override
    public void forEach(Consumer<? super Pair<String, Pair<Integer, Integer>>> action) {
        pif.forEach(action);
    }

    @Override
    public Spliterator<Pair<String, Pair<Integer, Integer>>> spliterator() {
        return pif.spliterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (var stringPairPair : pif) {
            sb.append("Token: \"" + stringPairPair.getKey() + "\", Positions: " + stringPairPair.getValue() + "\n");
        }
        return sb.toString();
    }
}
