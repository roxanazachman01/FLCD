package ro.flcd.domain.parsetree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ro.flcd.domain.grammar.Nonterminal;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ParseTree {
    private ParseTreeNode root;

    public ParseTree(Nonterminal startSymbol) {
        this.root = new ParseTreeNode(startSymbol);
    }
}
