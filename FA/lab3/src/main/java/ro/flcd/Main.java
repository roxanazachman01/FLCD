package ro.flcd;

public class Main {
    public static void main(String[] args) {
        /*
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
        System.out.println("\nSearching fdor non-existent symbol: " + symbolTable.search("lalalal") + "\n");
        System.out.println("Symbol Table: " + symbolTable);
        */

        Scanner scanner = new Scanner("in/serr3.txt", "in/token.in", "out/PIF.out", "out/ST.out");
        // the out files are written in the TARGET/classes/out because that's what java does
        scanner.tokenize();

//        final FiniteAutomata finiteAutomata = new FiniteAutomata("in/integerFA.in");
//
//        final MenuFA menuFA = new MenuFA(finiteAutomata);
//        menuFA.run();


    }
    //cerinta, analiza si proiectare, implementare, exemple de test; diagrame?????
}