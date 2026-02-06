package org.example.domain.policy;

import org.example.domain.tag.Tag;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProfanityPolicy implements TagPolicy {

    private final Set<String> bannedWords;

    public ProfanityPolicy(Set<String> bannedWords) {
        this.bannedWords = bannedWords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public PolicyResult evaluate(Tag tag, List<Tag> existingTags) {
        if (bannedWords.contains(tag.name().toLowerCase())) {
            return PolicyResult.rejected("Tag contains profanity: " + tag.name());
        }
        return PolicyResult.approved();
    }
}
