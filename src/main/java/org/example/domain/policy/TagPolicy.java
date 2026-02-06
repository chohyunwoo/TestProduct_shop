package org.example.domain.policy;

import org.example.domain.tag.Tag;

import java.util.List;

public interface TagPolicy {

    PolicyResult evaluate(Tag tag, List<Tag> existingTags);
}
