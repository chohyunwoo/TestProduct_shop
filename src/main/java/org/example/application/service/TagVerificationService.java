package org.example.application.service;

import org.example.application.port.in.VerifyTagsUseCase;
import org.example.domain.policy.PolicyResult;
import org.example.domain.product.ProductId;
import org.example.domain.tag.Tag;
import org.example.domain.tag.TagRepository;

import java.util.List;

public class TagVerificationService implements VerifyTagsUseCase {

    private final TagRepository tagRepository;
    private final TagVerificationPipeline pipeline;

    public TagVerificationService(TagRepository tagRepository, TagVerificationPipeline pipeline) {
        this.tagRepository = tagRepository;
        this.pipeline = pipeline;
    }

    @Override
    public void verifyTags(ProductId productId) {
        List<Tag> tags = tagRepository.findByProductId(productId);
        List<Tag> approvedTags = tagRepository.findApprovedByProductId(productId);

        for (Tag tag : tags) {
            if (!tag.isPending()) {
                continue;
            }
            PolicyResult result = pipeline.verify(tag, approvedTags);
            if (result.isApproved()) {
                tag.approve();
                approvedTags = tagRepository.findApprovedByProductId(productId);
            } else {
                tag.reject(result.reason());
            }
        }
    }
}
