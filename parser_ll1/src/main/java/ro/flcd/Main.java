package ro.flcd;

public class Main {
    public static void main(String[] args) {
//        final Grammar grammar = new Grammar("in/g2.txt");
//        System.out.println("Non-terminals: "+grammar.getNonterminals()+"\n");
//        System.out.println("Terminals: "+grammar.getTerminals()+"\n");
//        System.out.println("Productions:\n"+grammar.getProductions()+"\n");
//        System.out.println("Starting symbol: "+grammar.getStartSymbol()+"\n");
//        System.out.println(grammar.isCFG()?"Is cfg":"Is not cfg");
//        System.out.println(grammar.getProductionsOfNonterminal(new Nonterminal("type")));


        Parser parser = new Parser("in/g4.txt");
        System.out.println("First: "+parser.getFirst());
        System.out.println("Follow: "+parser.getFollow());

    }
}