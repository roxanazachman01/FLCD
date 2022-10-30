package ro.flcd;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SymbolTableTest {

    @Test
    void stTest() {
        final SymbolTable symbolTable = new SymbolTable();
        List<String> symbols = List.of("test", "a", "c", "b",
                String.valueOf(-2), String.valueOf(0), String.valueOf(1),
                "print", "cba", "nice message", "abc", "bac");

        assertThat(symbolTable.getSize()).isEqualTo(0);
        for (final String elem : symbols) {
            var pos=symbolTable.insert(elem);
            assertThat(pos.getKey()).isEqualTo(elem.chars().sum()%997);

        }
        assertThat(symbolTable.getSize()).isEqualTo(12);

        for (final String elem : symbols) {
            assertThat(symbolTable.search(elem).getKey()).isEqualTo(elem.chars().sum()%997);
        }
        assertThat(symbolTable.search("lalalal").getKey()).isEqualTo(-1);
        assertThat(symbolTable.search("lalalal").getValue()).isEqualTo(-1);
    }

}