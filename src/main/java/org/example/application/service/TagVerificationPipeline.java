package org.example.application.service;

import org.example.domain.policy.PolicyResult;
import org.example.domain.policy.TagPolicy;
import org.example.domain.tag.Tag;

import java.util.List;

public class TagVerificationPipeline {

    private final List<TagPolicy> policies;

    public TagVerificationPipeline(List<TagPolicy> policies) {
        this.policies = policies;
    }

    public PolicyResult verify(Tag tag, List<Tag> existingTags) {
        for (TagPolicy policy : policies) {
            PolicyResult result = policy.evaluate(tag, existingTags);
            if (result.isRejected()) {
                return result; // fail-fast
            }
        }
        return PolicyResult.approved();
    }
}
