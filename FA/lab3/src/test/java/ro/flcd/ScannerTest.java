package ro.flcd;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScannerTest {

    @Test
    void tokenize() {
        Scanner scanner = new Scanner("in/p2.txt", "in/token.in", "out/PIF.out", "out/ST.out");
        scanner.tokenize();
        var pif = scanner.getPif();
        var st = scanner.getSymbolTable();
        var errors = scanner.getErrors();
        assertThat(errors.trim()).isEmpty();
        assertThat(st.search("x").getKey()).isNotEqualTo(-1);
        assertThat(st.search("y").getKey()).isNotEqualTo(-1);
        for (var pair : pif) {
            if (pair.getKey().equals("x")) {
                assertThat(st.search("x").getKey()).isEqualTo(pair.getValue().getKey());
            }
            if (pair.getKey().equals("y")) {
                assertThat(st.search("y").getKey()).isEqualTo(pair.getValue().getKey());
            }
        }

        scanner = new Scanner("in/p1err.txt", "in/token.in", "out/PIF.out", "out/ST.out");
        scanner.tokenize();
        errors = scanner.getErrors();
        assertThat(errors).isNotEmpty();
    }
}