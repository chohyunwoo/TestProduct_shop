package org.example.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void create_withValidArgs_succeeds() {
        Product product = Product.create("무선 이어폰", "고품질 블루투스 이어폰", Category.ELECTRONICS);

        assertNotNull(product.id());
        assertEquals("무선 이어폰", product.name());
        assertEquals("고품질 블루투스 이어폰", product.description());
        assertEquals(Category.ELECTRONICS, product.category());
    }

    @Test
    void create_withNullName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Product.create(null, "desc", Category.ELECTRONICS));
    }

    @Test
    void create_withBlankName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Product.create("  ", "desc", Category.ELECTRONICS));
    }

    @Test
    void create_withNullDescription_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Product.create("name", null, Category.ELECTRONICS));
    }

    @Test
    void create_withNullCategory_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Product.create("name", "desc", null));
    }

    @Test
    void create_withSpecificId_succeeds() {
        ProductId id = ProductId.of("product-1");
        Product product = Product.of(id, "이어폰", "설명", Category.ELECTRONICS);

        assertEquals(id, product.id());
    }
}
