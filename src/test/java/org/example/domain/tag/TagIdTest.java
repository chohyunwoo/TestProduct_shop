package org.example.domain.tag;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagIdTest {

    @Test
    void create_withValidUUID_succeeds() {
        TagId id = TagId.generate();
        assertNotNull(id);
        assertNotNull(id.value());
    }

    @Test
    void create_withSpecificValue_succeeds() {
        String value = "tag-456";
        TagId id = TagId.of(value);
        assertEquals(value, id.value());
    }

    @Test
    void create_withNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> TagId.of(null));
    }

    @Test
    void create_withBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> TagId.of("  "));
    }

    @Test
    void equality_sameValue_areEqual() {
        TagId id1 = TagId.of("abc");
        TagId id2 = TagId.of("abc");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equality_differentValue_areNotEqual() {
        TagId id1 = TagId.of("abc");
        TagId id2 = TagId.of("def");
        assertNotEquals(id1, id2);
    }
}
