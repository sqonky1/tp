package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PrefixTest {

    @Test
    public void hashCode_nullPrefix_returnsZero() {
        Prefix prefix = new Prefix(null);
        assertEquals(0, prefix.hashCode());
    }

    @Test
    public void hashCode_caseInsensitive() {
        Prefix lower = new Prefix("n/");
        Prefix upper = new Prefix("N/");
        assertEquals(lower.hashCode(), upper.hashCode());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Prefix prefix = new Prefix("n/");
        assertTrue(prefix.equals(prefix));
    }

    @Test
    public void equals_null_returnsFalse() {
        Prefix prefix = new Prefix("n/");
        assertFalse(prefix.equals(null));
    }

    @Test
    public void equals_nonPrefixObject_returnsFalse() {
        Prefix prefix = new Prefix("n/");
        assertFalse(prefix.equals("n/"));
    }

    @Test
    public void equals_differentCase_returnsTrue() {
        Prefix lower = new Prefix("n/");
        Prefix upper = new Prefix("N/");
        assertTrue(lower.equals(upper));
    }

    @Test
    public void equals_samePrefix_returnsTrue() {
        Prefix p1 = new Prefix("n/");
        Prefix p2 = new Prefix("n/");
        assertTrue(p1.equals(p2));
    }

    @Test
    public void equals_differentPrefix_returnsFalse() {
        Prefix p1 = new Prefix("n/");
        Prefix p2 = new Prefix("t/");
        assertFalse(p1.equals(p2));
    }
}
