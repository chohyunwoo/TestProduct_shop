package org.example.application.service;

import org.example.domain.policy.*;
import org.example.domain.product.Category;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TagVerificationPipelineTest {

    private Tag createTag(String name, double confidence) {
        return Tag.createPending(ProductId.of("p-1"), name, ConfidenceScore.of(confidence), Category.ELECTRONICS);
    }

    @Test
    void verify_allPoliciesPass_returnsApproved() {
        TagVerificationPipeline pipeline = new TagVerificationPipeline(List.of(
                new MinConfidencePolicy(0.5),
                new ProfanityPolicy(Set.of("badword"))
        ));

        Tag tag = createTag("wireless", 0.9);
        PolicyResult result = pipeline.verify(tag, List.of());

        assertTrue(result.isApproved());
    }

    @Test
    void verify_firstPolicyFails_failsFast() {
        TagVerificationPipeline pipeline = new TagVerificationPipeline(List.of(
                new MinConfidencePolicy(0.5),
                new ProfanityPolicy(Set.of("badword"))
        ));

        Tag tag = createTag("wireless", 0.3);
        PolicyResult result = pipeline.verify(tag, List.of());

        assertTrue(result.isRejected());
        assertTrue(result.reason().contains("confidence"));
    }

    @Test
    void verify_secondPolicyFails_returnsRejected() {
        TagVerificationPipeline pipeline = new TagVerificationPipeline(List.of(
                new MinConfidencePolicy(0.5),
                new ProfanityPolicy(Set.of("badword"))
        ));

        Tag tag = createTag("badword", 0.9);
        PolicyResult result = pipeline.verify(tag, List.of());

        assertTrue(result.isRejected());
        assertTrue(result.reason().contains("profanity"));
    }

    @Test
    void verify_noPolicies_returnsApproved() {
        TagVerificationPipeline pipeline = new TagVerificationPipeline(List.of());

        Tag tag = createTag("anything", 0.1);
        PolicyResult result = pipeline.verify(tag, List.of());

        assertTrue(result.isApproved());
    }
}
