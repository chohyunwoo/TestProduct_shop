package org.example.domain.policy;

import org.example.domain.product.Category;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProfanityPolicyTest {

    private final ProfanityPolicy policy = new ProfanityPolicy(Set.of("badword", "offensive"));

    private Tag tagWithName(String name) {
        return Tag.createPending(ProductId.of("p-1"), name, ConfidenceScore.of(0.9), Category.ELECTRONICS);
    }

    @Test
    void evaluate_cleanTag_approved() {
        Tag tag = tagWithName("wireless");
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isApproved());
    }

    @Test
    void evaluate_profaneTag_rejected() {
        Tag tag = tagWithName("badword");
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isRejected());
        assertTrue(result.reason().contains("profanity"));
    }

    @Test
    void evaluate_profaneTagCaseInsensitive_rejected() {
        Tag tag = tagWithName("OFFENSIVE");
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isRejected());
    }
}
