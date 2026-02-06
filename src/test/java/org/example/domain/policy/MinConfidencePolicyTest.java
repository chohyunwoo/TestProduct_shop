package org.example.domain.policy;

import org.example.domain.product.Category;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinConfidencePolicyTest {

    private final MinConfidencePolicy policy = new MinConfidencePolicy(0.5);

    private Tag tagWithConfidence(double confidence) {
        return Tag.createPending(ProductId.of("p-1"), "test-tag", ConfidenceScore.of(confidence), Category.ELECTRONICS);
    }

    @Test
    void evaluate_aboveThreshold_approved() {
        Tag tag = tagWithConfidence(0.8);
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isApproved());
    }

    @Test
    void evaluate_atThreshold_rejected() {
        Tag tag = tagWithConfidence(0.5);
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isRejected());
    }

    @Test
    void evaluate_belowThreshold_rejected() {
        Tag tag = tagWithConfidence(0.3);
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isRejected());
        assertTrue(result.reason().contains("confidence"));
    }
}
