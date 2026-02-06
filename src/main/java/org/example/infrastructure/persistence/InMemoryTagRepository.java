package org.example.infrastructure.persistence;

import org.example.domain.product.ProductId;
import org.example.domain.tag.Tag;
import org.example.domain.tag.TagRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTagRepository implements TagRepository {

    private final List<Tag> store = new ArrayList<>();

    @Override
    public void save(Tag tag) {
        store.add(tag);
    }

    @Override
    public List<Tag> findByProductId(ProductId productId) {
        return store.stream()
                .filter(tag -> tag.productId().equals(productId))
                .toList();
    }

    @Override
    public List<Tag> findApprovedByProductId(ProductId productId) {
        return store.stream()
                .filter(tag -> tag.productId().equals(productId))
                .filter(Tag::isApproved)
                .toList();
    }
}
