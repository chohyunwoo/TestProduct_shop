package org.example.infrastructure.persistence;

import org.example.domain.product.Category;
import org.example.domain.product.Product;
import org.example.domain.product.ProductId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryProductRepositoryTest {

    private final InMemoryProductRepository repository = new InMemoryProductRepository();

    @Test
    void save_andFindById_succeeds() {
        Product product = Product.create("이어폰", "블루투스 이어폰", Category.ELECTRONICS);
        repository.save(product);

        Optional<Product> found = repository.findById(product.id());
        assertTrue(found.isPresent());
        assertEquals(product.name(), found.get().name());
    }

    @Test
    void findById_notExists_returnsEmpty() {
        Optional<Product> found = repository.findById(ProductId.of("nonexistent"));
        assertTrue(found.isEmpty());
    }

    @Test
    void save_overwriteExisting_succeeds() {
        ProductId id = ProductId.of("p-1");
        Product original = Product.of(id, "이어폰", "원본", Category.ELECTRONICS);
        Product updated = Product.of(id, "이어폰", "수정됨", Category.ELECTRONICS);

        repository.save(original);
        repository.save(updated);

        Optional<Product> found = repository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("수정됨", found.get().description());
    }
}
