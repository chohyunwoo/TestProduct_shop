package org.example.domain.policy;

import org.example.domain.tag.Tag;

import java.util.List;

public class MaxTagCountPolicy implements TagPolicy {

    private final int maxTags;

    public MaxTagCountPolicy(int maxTags) {
        this.maxTags = maxTags;
    }

    @Override
    public PolicyResult evaluate(Tag tag, List<Tag> existingTags) {
        if (existingTags.size() >= maxTags) {
            return PolicyResult.rejected(
                    String.format("Tag count exceeds maximum of %d (current: %d)", maxTags, existingTags.size())
            );
        }
        return PolicyResult.approved();
    }
}
