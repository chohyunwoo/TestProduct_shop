package org.example.domain.tag;

import org.example.domain.product.Category;
import org.example.domain.product.ProductId;

public class Tag {

    private final TagId id;
    private final ProductId productId;
    private final String name;
    private final ConfidenceScore confidence;
    private final Category suggestedCategory;
    private TagStatus status;
    private String rejectionReason;

    private Tag(TagId id, ProductId productId, String name, ConfidenceScore confidence, Category suggestedCategory) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.confidence = confidence;
        this.suggestedCategory = suggestedCategory;
        this.status = TagStatus.PENDING;
    }

    public static Tag createPending(ProductId productId, String name, ConfidenceScore confidence, Category suggestedCategory) {
        return new Tag(TagId.generate(), productId, name, confidence, suggestedCategory);
    }

    public void approve() {
        if (status != TagStatus.PENDING) {
            throw new IllegalStateException("Can only approve a PENDING tag, current status: " + status);
        }
        this.status = TagStatus.APPROVED;
    }

    public void reject(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason must not be null or blank");
        }
        if (status != TagStatus.PENDING) {
            throw new IllegalStateException("Can only reject a PENDING tag, current status: " + status);
        }
        this.status = TagStatus.REJECTED;
        this.rejectionReason = reason;
    }

    public boolean isPending() {
        return status == TagStatus.PENDING;
    }

    public boolean isApproved() {
        return status == TagStatus.APPROVED;
    }

    public TagId id() {
        return id;
    }

    public ProductId productId() {
        return productId;
    }

    public String name() {
        return name;
    }

    public ConfidenceScore confidence() {
        return confidence;
    }

    public Category suggestedCategory() {
        return suggestedCategory;
    }

    public TagStatus status() {
        return status;
    }

    public String rejectionReason() {
        return rejectionReason;
    }
}
