package ro.flcd.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Production {
    private final String leftHS;
    private List<TermOrNonTerm> rightHS;
}
