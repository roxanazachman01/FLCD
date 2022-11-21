package ro.flcd;

public class Main {
    public static void main(String[] args) {
        final Grammar grammar = new Grammar("in/grammar.in");
        System.out.println(grammar.getTerminals());
        System.out.println(grammar.getNonterminals());
        System.out.println(grammar.getProductions());
        System.out.println(grammar.getStartSymbol());
    }
}