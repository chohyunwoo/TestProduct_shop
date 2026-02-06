package org.example.domain.policy;

import org.example.domain.product.Category;
import org.example.domain.tag.Tag;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryRelevancePolicy implements TagPolicy {

    private final Map<Category, Set<String>> categoryKeywords;

    public CategoryRelevancePolicy(Map<Category, Set<String>> categoryKeywords) {
        this.categoryKeywords = categoryKeywords.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(String::toLowerCase).collect(Collectors.toSet())
                ));
    }

    @Override
    public PolicyResult evaluate(Tag tag, List<Tag> existingTags) {
        String tagName = tag.name().toLowerCase();
        Category tagCategory = tag.suggestedCategory();

        // If the category has no keyword mapping, allow any tag
        Set<String> relevantKeywords = categoryKeywords.get(tagCategory);
        if (relevantKeywords == null) {
            return PolicyResult.approved();
        }

        // If the tag name isn't in any category's keyword set, allow it (generic tag)
        boolean isKnownKeyword = categoryKeywords.values().stream()
                .anyMatch(keywords -> keywords.contains(tagName));
        if (!isKnownKeyword) {
            return PolicyResult.approved();
        }

        // If the tag is a known keyword but belongs to a different category, reject
        if (!relevantKeywords.contains(tagName)) {
            return PolicyResult.rejected(
                    String.format("Tag '%s' is not relevant to category %s", tag.name(), tagCategory)
            );
        }

        return PolicyResult.approved();
    }
}
