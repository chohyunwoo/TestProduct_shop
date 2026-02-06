package org.example.domain.policy;

import org.example.domain.product.Category;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxTagCountPolicyTest {

    private final MaxTagCountPolicy policy = new MaxTagCountPolicy(3);

    private Tag createTag(String name) {
        return Tag.createPending(ProductId.of("p-1"), name, ConfidenceScore.of(0.9), Category.ELECTRONICS);
    }

    @Test
    void evaluate_belowMax_approved() {
        Tag tag = createTag("new-tag");
        List<Tag> existing = List.of(createTag("tag1"), createTag("tag2"));
        PolicyResult result = policy.evaluate(tag, existing);
        assertTrue(result.isApproved());
    }

    @Test
    void evaluate_atMax_rejected() {
        Tag tag = createTag("new-tag");
        List<Tag> existing = List.of(createTag("tag1"), createTag("tag2"), createTag("tag3"));
        PolicyResult result = policy.evaluate(tag, existing);
        assertTrue(result.isRejected());
        assertTrue(result.reason().contains("maximum"));
    }

    @Test
    void evaluate_aboveMax_rejected() {
        Tag tag = createTag("new-tag");
        List<Tag> existing = List.of(createTag("t1"), createTag("t2"), createTag("t3"), createTag("t4"));
        PolicyResult result = policy.evaluate(tag, existing);
        assertTrue(result.isRejected());
    }

    @Test
    void evaluate_emptyExisting_approved() {
        Tag tag = createTag("new-tag");
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isApproved());
    }
}
