package ro.flcd.domain.parsetree;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ro.flcd.domain.grammar.TermOrNonTerm;

@Getter
@Setter
public class ParseTreeNode {
    private TermOrNonTerm value;
    private ParseTreeNode parent;
    private ParseTreeNode leftSibling;
    private ParseTreeNode rightSibling;

    public ParseTreeNode(TermOrNonTerm value) {
        this.value = value;
        this.parent = null;
        this.leftSibling = null;
        this.rightSibling = null;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}