package org.example.web.dto;

import org.example.domain.product.Category;
import org.example.domain.tag.Tag;
import org.example.domain.tag.TagStatus;

public record TagResponse(String id,
                          String name,
                          double confidence,
                          Category suggestedCategory,
                          TagStatus status,
                          String rejectionReason) {

    public static TagResponse from(Tag tag) {
        return new TagResponse(
                tag.id().value(),
                tag.name(),
                tag.confidence().value(),
                tag.suggestedCategory(),
                tag.status(),
                tag.rejectionReason()
        );
    }
}
