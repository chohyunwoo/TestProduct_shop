package org.example.domain.product;

public class Product {

    private final ProductId id;
    private final String name;
    private final String description;
    private final Category category;

    private Product(ProductId id, String name, String description, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public static Product create(String name, String description, Category category) {
        validateArgs(name, description, category);
        return new Product(ProductId.generate(), name, description, category);
    }

    public static Product of(ProductId id, String name, String description, Category category) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        validateArgs(name, description, category);
        return new Product(id, name, description, category);
    }

    private static void validateArgs(String name, String description, Category category) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }
        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("category must not be null");
        }
    }

    public ProductId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Category category() {
        return category;
    }
}
