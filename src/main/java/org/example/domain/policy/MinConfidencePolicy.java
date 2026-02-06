package org.example.domain.policy;

import org.example.domain.tag.Tag;

import java.util.List;

public class MinConfidencePolicy implements TagPolicy {

    private final double threshold;

    public MinConfidencePolicy(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public PolicyResult evaluate(Tag tag, List<Tag> existingTags) {
        if (tag.confidence().isAbove(threshold)) {
            return PolicyResult.approved();
        }
        return PolicyResult.rejected(
                String.format("Tag confidence %.2f does not exceed minimum threshold %.2f", tag.confidence().value(), threshold)
        );
    }
}
