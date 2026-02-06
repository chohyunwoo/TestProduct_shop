package org.example.domain.policy;

import org.example.domain.tag.Tag;

import java.util.List;

public class DuplicateTagPolicy implements TagPolicy {

    @Override
    public PolicyResult evaluate(Tag tag, List<Tag> existingTags) {
        boolean isDuplicate = existingTags.stream()
                .anyMatch(existing -> existing.name().equalsIgnoreCase(tag.name()));
        if (isDuplicate) {
            return PolicyResult.rejected("Tag is a duplicate: " + tag.name());
        }
        return PolicyResult.approved();
    }
}
