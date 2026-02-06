package org.example.application.service;

import org.example.domain.product.Category;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.example.infrastructure.persistence.InMemoryTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApprovedTagQueryServiceTest {

    private InMemoryTagRepository tagRepository;
    private ApprovedTagQueryService service;

    @BeforeEach
    void setUp() {
        tagRepository = new InMemoryTagRepository();
        service = new ApprovedTagQueryService(tagRepository);
    }

    @Test
    void getApprovedTags_returnsOnlyApproved() {
        ProductId productId = ProductId.of("p-1");

        Tag approved = Tag.createPending(productId, "wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS);
        approved.approve();

        Tag pending = Tag.createPending(productId, "bluetooth", ConfidenceScore.of(0.8), Category.ELECTRONICS);

        Tag rejected = Tag.createPending(productId, "bad", ConfidenceScore.of(0.2), Category.ELECTRONICS);
        rejected.reject("low confidence");

        tagRepository.save(approved);
        tagRepository.save(pending);
        tagRepository.save(rejected);

        List<Tag> result = service.getApprovedTags(productId);

        assertEquals(1, result.size());
        assertEquals("wireless", result.get(0).name());
    }

    @Test
    void getApprovedTags_noApproved_returnsEmpty() {
        ProductId productId = ProductId.of("p-1");

        Tag pending = Tag.createPending(productId, "wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS);
        tagRepository.save(pending);

        List<Tag> result = service.getApprovedTags(productId);
        assertTrue(result.isEmpty());
    }
}
