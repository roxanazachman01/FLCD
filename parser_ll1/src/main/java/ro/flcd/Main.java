package ro.flcd;

import ro.flcd.domain.grammar.Terminal;
import ro.flcd.domain.parsingtable.ParsingTable;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        final Grammar grammar = new Grammar("in/g2.txt");
//        System.out.println("Non-terminals: "+grammar.getNonterminals()+"\n");
//        System.out.println("Terminals: "+grammar.getTerminals()+"\n");
//        System.out.println("Productions:\n"+grammar.getProductions()+"\n");
//        System.out.println("Starting symbol: "+grammar.getStartSymbol()+"\n");
//        System.out.println(grammar.isCFG()?"Is cfg":"Is not cfg");
//        System.out.println(grammar.getProductionsOfNonterminal(new Nonterminal("type")));


        Parser parser = new Parser("in/g3.txt");
//        System.out.println("First: "+parser.getFirst());
//        System.out.println("Follow: "+parser.getFollow());
        ParsingTable parsingTable = new ParsingTable(parser);
//        parsingTable.printParsingTable();
        List<Terminal> sequence = List.of(new Terminal("a"),new Terminal("*"),new Terminal("("),new Terminal("a"),new Terminal("+"),new Terminal("a"),new Terminal(")"));
        parsingTable.parseSequence(sequence);
    }
}