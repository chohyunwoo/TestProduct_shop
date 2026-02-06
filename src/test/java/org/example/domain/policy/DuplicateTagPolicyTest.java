package org.example.domain.policy;

import org.example.domain.product.Category;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateTagPolicyTest {

    private final DuplicateTagPolicy policy = new DuplicateTagPolicy();

    private Tag createTag(String name) {
        return Tag.createPending(ProductId.of("p-1"), name, ConfidenceScore.of(0.9), Category.ELECTRONICS);
    }

    @Test
    void evaluate_noDuplicates_approved() {
        Tag tag = createTag("wireless");
        Tag existing = createTag("bluetooth");
        PolicyResult result = policy.evaluate(tag, List.of(existing));
        assertTrue(result.isApproved());
    }

    @Test
    void evaluate_duplicateExists_rejected() {
        Tag tag = createTag("wireless");
        Tag existing = createTag("wireless");
        PolicyResult result = policy.evaluate(tag, List.of(existing));
        assertTrue(result.isRejected());
        assertTrue(result.reason().contains("duplicate"));
    }

    @Test
    void evaluate_emptyExisting_approved() {
        Tag tag = createTag("wireless");
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isApproved());
    }

    @Test
    void evaluate_caseInsensitiveDuplicate_rejected() {
        Tag tag = createTag("wireless");
        Tag existing = createTag("Wireless");
        PolicyResult result = policy.evaluate(tag, List.of(existing));
        assertTrue(result.isRejected());
    }
}
