package org.example.application.service;

import org.example.domain.policy.MinConfidencePolicy;
import org.example.domain.policy.ProfanityPolicy;
import org.example.domain.product.Category;
import org.example.domain.product.Product;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.example.domain.tag.TagStatus;
import org.example.infrastructure.persistence.InMemoryTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TagVerificationServiceTest {

    private InMemoryTagRepository tagRepository;
    private TagVerificationService service;

    @BeforeEach
    void setUp() {
        tagRepository = new InMemoryTagRepository();
        TagVerificationPipeline pipeline = new TagVerificationPipeline(List.of(
                new MinConfidencePolicy(0.5),
                new ProfanityPolicy(Set.of("badword"))
        ));
        service = new TagVerificationService(tagRepository, pipeline);
    }

    private Tag createAndSaveTag(String name, double confidence) {
        Tag tag = Tag.createPending(ProductId.of("p-1"), name, ConfidenceScore.of(confidence), Category.ELECTRONICS);
        tagRepository.save(tag);
        return tag;
    }

    @Test
    void verifyTags_approvesValidTags() {
        createAndSaveTag("wireless", 0.9);
        createAndSaveTag("bluetooth", 0.8);

        service.verifyTags(ProductId.of("p-1"));

        List<Tag> tags = tagRepository.findByProductId(ProductId.of("p-1"));
        assertTrue(tags.stream().allMatch(t -> t.status() == TagStatus.APPROVED));
    }

    @Test
    void verifyTags_rejectsLowConfidenceTags() {
        createAndSaveTag("wireless", 0.3);

        service.verifyTags(ProductId.of("p-1"));

        List<Tag> tags = tagRepository.findByProductId(ProductId.of("p-1"));
        assertEquals(TagStatus.REJECTED, tags.get(0).status());
        assertNotNull(tags.get(0).rejectionReason());
    }

    @Test
    void verifyTags_rejectsProfaneTags() {
        createAndSaveTag("badword", 0.9);

        service.verifyTags(ProductId.of("p-1"));

        List<Tag> tags = tagRepository.findByProductId(ProductId.of("p-1"));
        assertEquals(TagStatus.REJECTED, tags.get(0).status());
    }

    @Test
    void verifyTags_mixedResults() {
        createAndSaveTag("wireless", 0.9);  // should pass
        createAndSaveTag("badword", 0.9);   // should fail (profanity)
        createAndSaveTag("cheap", 0.3);     // should fail (low confidence)

        service.verifyTags(ProductId.of("p-1"));

        List<Tag> tags = tagRepository.findByProductId(ProductId.of("p-1"));
        long approved = tags.stream().filter(t -> t.status() == TagStatus.APPROVED).count();
        long rejected = tags.stream().filter(t -> t.status() == TagStatus.REJECTED).count();

        assertEquals(1, approved);
        assertEquals(2, rejected);
    }

    @Test
    void verifyTags_skipsAlreadyProcessedTags() {
        Tag tag = createAndSaveTag("wireless", 0.9);
        tag.approve(); // pre-approved

        // Should not throw - already processed tags are skipped
        service.verifyTags(ProductId.of("p-1"));

        assertEquals(TagStatus.APPROVED, tag.status());
    }
}
