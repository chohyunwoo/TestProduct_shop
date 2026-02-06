package org.example.application.service;

import org.example.application.port.in.GetApprovedTagsUseCase;
import org.example.domain.product.ProductId;
import org.example.domain.tag.Tag;
import org.example.domain.tag.TagRepository;

import java.util.List;

public class ApprovedTagQueryService implements GetApprovedTagsUseCase {

    private final TagRepository tagRepository;

    public ApprovedTagQueryService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getApprovedTags(ProductId productId) {
        return tagRepository.findApprovedByProductId(productId);
    }
}
