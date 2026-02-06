package org.example.infrastructure.persistence;

import org.example.domain.product.Category;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTagRepositoryTest {

    private final InMemoryTagRepository repository = new InMemoryTagRepository();

    private Tag createTag(String productId, String name) {
        return Tag.createPending(ProductId.of(productId), name, ConfidenceScore.of(0.9), Category.ELECTRONICS);
    }

    @Test
    void save_andFindByProductId_succeeds() {
        Tag tag = createTag("p-1", "wireless");
        repository.save(tag);

        List<Tag> found = repository.findByProductId(ProductId.of("p-1"));
        assertEquals(1, found.size());
        assertEquals("wireless", found.get(0).name());
    }

    @Test
    void findByProductId_multipleProducts_filtersCorrectly() {
        repository.save(createTag("p-1", "wireless"));
        repository.save(createTag("p-1", "bluetooth"));
        repository.save(createTag("p-2", "cotton"));

        List<Tag> tagsP1 = repository.findByProductId(ProductId.of("p-1"));
        assertEquals(2, tagsP1.size());

        List<Tag> tagsP2 = repository.findByProductId(ProductId.of("p-2"));
        assertEquals(1, tagsP2.size());
    }

    @Test
    void findApprovedByProductId_onlyReturnsApproved() {
        Tag approved = createTag("p-1", "wireless");
        approved.approve();

        Tag pending = createTag("p-1", "bluetooth");

        Tag rejected = createTag("p-1", "badtag");
        rejected.reject("profanity");

        repository.save(approved);
        repository.save(pending);
        repository.save(rejected);

        List<Tag> approvedTags = repository.findApprovedByProductId(ProductId.of("p-1"));
        assertEquals(1, approvedTags.size());
        assertEquals("wireless", approvedTags.get(0).name());
    }

    @Test
    void findByProductId_noTags_returnsEmpty() {
        List<Tag> found = repository.findByProductId(ProductId.of("nonexistent"));
        assertTrue(found.isEmpty());
    }
}
