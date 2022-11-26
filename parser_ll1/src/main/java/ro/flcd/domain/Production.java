package ro.flcd.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Production {
    private List<TermOrNonTerm> leftHS;
    private List<TermOrNonTerm> rightHS;

    @Override
    public String toString() {
        return leftHS.stream().map(TermOrNonTerm::value).collect(Collectors.joining("")) + " -> " + rightHS.stream().map(TermOrNonTerm::value).collect(Collectors.joining(" "));
    }
}
