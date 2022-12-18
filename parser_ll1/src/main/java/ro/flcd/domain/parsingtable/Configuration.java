package ro.flcd.domain.parsingtable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;
import ro.flcd.domain.grammar.Epsilon;
import ro.flcd.domain.grammar.Nonterminal;
import ro.flcd.domain.grammar.TermOrNonTerm;
import ro.flcd.domain.grammar.Terminal;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@ToString(exclude = "table")
public class Configuration {
    private final Map<Pair<TermOrNonTerm, TermOrNonTerm>, ParsingTableValue> table;
    List<Integer> productionsIndexes = new ArrayList<>();
    private Stack<TermOrNonTerm> inputStack;
    private Stack<TermOrNonTerm> workingStack;

    public Configuration(List<Integer> productionsIndexes, Map<Pair<TermOrNonTerm, TermOrNonTerm>, ParsingTableValue> table, Stack<TermOrNonTerm> inputStack, Stack<TermOrNonTerm> workingStack) {
        this.productionsIndexes = productionsIndexes;
        this.table = table;
        this.inputStack = inputStack;
        this.workingStack = workingStack;
    }

    public Configuration(Map<Pair<TermOrNonTerm, TermOrNonTerm>, ParsingTableValue> table, Stack<TermOrNonTerm> inputStack, Stack<TermOrNonTerm> workingStack) {
        this.table = table;
        this.inputStack = inputStack;
        this.workingStack = workingStack;
    }

    // result
    public static Configuration getInitialConfig(Map<Pair<TermOrNonTerm, TermOrNonTerm>, ParsingTableValue> table, List<Terminal> stringToParse, Nonterminal start) {
        Stack<TermOrNonTerm> input = new Stack<>();
        input.addAll(stringToParse);
        input.add(new DollarSign());

        Stack<TermOrNonTerm> working = new Stack<>();
        working.add(start);
        working.add(new DollarSign());
        return new Configuration(table, input, working);
    }

    public void push() {
        NormalTableValue normalTableValue = (NormalTableValue) table.get(Pair.of(workingStack.get(0), inputStack.get(0)));
        workingStack.remove(0);
        if (!normalTableValue.getRhs().contains(new Epsilon())) {
            workingStack.addAll(0, normalTableValue.getRhs());
        }
        productionsIndexes.add(normalTableValue.getProductionIndex());
    }

    public void pop() {
        inputStack.remove(0);
        workingStack.remove(0);
    }

    public boolean shouldPush() {
        return !table.get(Pair.of(workingStack.get(0), inputStack.get(0))).equals(SpecialTableValue.ERR) && !shouldPop() && !isAccepted();
    }

    public boolean shouldPop() {
        return table.get(Pair.of(workingStack.get(0), inputStack.get(0))).equals(SpecialTableValue.POP);
    }

    public boolean isAccepted() {
        return table.get(Pair.of(workingStack.get(0), inputStack.get(0))).equals(SpecialTableValue.ACC);
    }

    public Configuration deepCopy() {
        return new Configuration(new ArrayList<>(productionsIndexes),new HashMap<>(this.table), (Stack<TermOrNonTerm>) this.inputStack.clone(), (Stack<TermOrNonTerm>) this.workingStack.clone());
    }
}
