package org.example.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductIdTest {

    @Test
    void create_withValidUUID_succeeds() {
        ProductId id = ProductId.generate();
        assertNotNull(id);
        assertNotNull(id.value());
    }

    @Test
    void create_withSpecificValue_succeeds() {
        String value = "product-123";
        ProductId id = ProductId.of(value);
        assertEquals(value, id.value());
    }

    @Test
    void create_withNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ProductId.of(null));
    }

    @Test
    void create_withBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ProductId.of("  "));
    }

    @Test
    void equality_sameValue_areEqual() {
        ProductId id1 = ProductId.of("abc");
        ProductId id2 = ProductId.of("abc");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equality_differentValue_areNotEqual() {
        ProductId id1 = ProductId.of("abc");
        ProductId id2 = ProductId.of("def");
        assertNotEquals(id1, id2);
    }
}
