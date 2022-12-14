package ro.flcd;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class SymbolTable { //hashtable
    private final ArrayList<LinkedList<String>> table = new ArrayList<>();
    private int m = 997;
    private int size = 0;

    public SymbolTable() {
        IntStream.range(0, m).forEach(i -> table.add(new LinkedList<>()));
    }

    public SymbolTable(final int m) {
        this.m = m;
        IntStream.range(0, m).forEach(i -> table.add(new LinkedList<>()));

    }

    public int getSize() {
        return size;
    }

    /**
     * Computes the hash value of the symbol using the sum of ascii chars of the given symbol modulo m,
     * the fixed size of the array
     *
     * @param symbol : String representing the identifier or constant
     * @return : the result of the hash function
     */
    private int hash(final String symbol) {
        int sum_chars = symbol.chars().sum();
        return sum_chars % m;
    }

    /**
     * Searches for the position of the symbol in the symbol table.
     *
     * @param symbol : String representing the identifier or constant
     * @return : Pair<Integer,Integer> representing the position in the hash table and position in the linked list,
     * if it exists, or Pair(-1,-1) if it doesn't exist
     */
    public Pair<Integer, Integer> search(final String symbol) {
        int index = hash(symbol);
        final LinkedList<String> linkedList = table.get(index);
        if (linkedList != null) {
            int linkedListPosition = linkedList.indexOf(symbol);
            if (linkedListPosition != -1) {
                return new Pair<>(index, linkedListPosition);
            }
        }
        return new Pair<>(-1, -1);
    }

    /**
     * Inserts the symbol in the symbol table, if it doesn't already exist
     *
     * @param symbol : string representing identifier or constant
     * @return : Pair of integers representing position in hashtable and linkedlist
     */
    public Pair<Integer, Integer> insert(final String symbol) {
        int index = hash(symbol);
        final Pair<Integer, Integer> positions = search(symbol);
        if (positions.getKey() == -1 && positions.getValue() == -1) {
            LinkedList<String> linkedList = table.get(index);
            linkedList.add(symbol);
            this.size += 1;
            return new Pair<>(index, linkedList.indexOf(symbol));
        }
        return positions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int index = 0; index < table.size(); index++) {
            if (!table.get(index).isEmpty()) {
                for (int listIndex = 0; listIndex < table.get(index).size(); listIndex++) {
                    sb.append("(" + index + ", " + listIndex + ")" + ": " + table.get(index).get(listIndex) + "\n");
                }
            }

        }
        return sb.toString();
    }
}
