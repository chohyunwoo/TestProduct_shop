package org.example.web.dto;

import org.example.domain.product.Product;
import org.example.domain.product.Category;

public record ProductResponse(String id, String name, String description, Category category) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.id().value(),
                product.name(),
                product.description(),
                product.category()
        );
    }
}
