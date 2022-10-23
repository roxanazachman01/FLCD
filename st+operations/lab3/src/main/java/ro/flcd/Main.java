package ro.flcd;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final SymbolTable symbolTable = new SymbolTable();
        List<String> symbols = List.of("test", "a", "c", "b",
                String.valueOf(-2), String.valueOf(0), String.valueOf(1),
                "print", "cba", "nice message", "abc", "bac");
        for (final String elem : symbols) {
            symbolTable.insert(elem);
        }
        for (final String elem : symbols) {
            var pos = symbolTable.search(elem);
            System.out.println(elem + " ---- pos1: " + pos.getKey() + "; pos2: " + pos.getValue());
        }
        System.out.println("\nSearching for non-existent symbol: " + symbolTable.search("lalalal") + "\n");
        System.out.println("Symbol Table: " + symbolTable);
    }
    //cerinta, analiza si proiectare, implementare, exemple de test; diagrame?????
}