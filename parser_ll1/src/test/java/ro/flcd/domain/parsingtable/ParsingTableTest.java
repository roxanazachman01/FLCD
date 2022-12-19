package ro.flcd.domain.parsingtable;

import org.junit.Test;
import ro.flcd.Parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParsingTableTest {

    @Test
    public void parseSequenceFromFile() {
        Parser parser = new Parser("in/g3.txt");
        ParsingTable parsingTable = new ParsingTable(parser);
        assertTrue(parsingTable.parseSequenceFromFile("in/seq3.txt"));

        assertFalse(parsingTable.parseSequenceFromFile("in/seq3err.txt"));
    }

    @Test
    public void parseSequenceFromPif() {
        Parser parser = new Parser("in/g2.txt");
        ParsingTable parsingTable = new ParsingTable(parser);
        assertTrue(parsingTable.parseSequenceFromPif("in/pif1.txt"));
        assertTrue(parsingTable.parseSequenceFromPif("in/pif2.txt"));
        assertTrue(parsingTable.parseSequenceFromPif("in/pif3.txt"));

        assertFalse(parsingTable.parseSequenceFromPif("in/pifserr1.txt"));
        assertFalse(parsingTable.parseSequenceFromPif("in/pifserr2.txt"));
        assertFalse(parsingTable.parseSequenceFromPif("in/pifserr3.txt"));
    }
}