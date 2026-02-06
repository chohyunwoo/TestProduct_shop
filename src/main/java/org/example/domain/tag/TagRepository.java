package org.example.domain.tag;

import org.example.domain.product.ProductId;

import java.util.List;

public interface TagRepository {

    void save(Tag tag);

    List<Tag> findByProductId(ProductId productId);

    List<Tag> findApprovedByProductId(ProductId productId);
}
