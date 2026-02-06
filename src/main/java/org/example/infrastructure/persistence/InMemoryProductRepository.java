package org.example.infrastructure.persistence;

import org.example.domain.product.Product;
import org.example.domain.product.ProductId;
import org.example.domain.product.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<ProductId, Product> store = new HashMap<>();

    @Override
    public void save(Product product) {
        store.put(product.id(), product);
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return Optional.ofNullable(store.get(id));
    }
}
