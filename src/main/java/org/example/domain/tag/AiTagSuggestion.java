package org.example.domain.tag;

import org.example.domain.product.Category;

public final class AiTagSuggestion {

    private final String tagName;
    private final ConfidenceScore confidence;
    private final Category suggestedCategory;

    public AiTagSuggestion(String tagName, ConfidenceScore confidence, Category suggestedCategory) {
        if (tagName == null || tagName.isBlank()) {
            throw new IllegalArgumentException("tagName must not be null or blank");
        }
        if (confidence == null) {
            throw new IllegalArgumentException("confidence must not be null");
        }
        if (suggestedCategory == null) {
            throw new IllegalArgumentException("suggestedCategory must not be null");
        }
        this.tagName = tagName.trim().toLowerCase();
        this.confidence = confidence;
        this.suggestedCategory = suggestedCategory;
    }

    public String tagName() {
        return tagName;
    }

    public ConfidenceScore confidence() {
        return confidence;
    }

    public Category suggestedCategory() {
        return suggestedCategory;
    }

    @Override
    public String toString() {
        return "AiTagSuggestion{" + tagName + ", " + confidence + ", " + suggestedCategory + "}";
    }
}
