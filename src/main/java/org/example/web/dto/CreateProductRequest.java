package org.example.web.dto;

import org.example.domain.product.Category;

public record CreateProductRequest(String name, String description, Category category) {
}
