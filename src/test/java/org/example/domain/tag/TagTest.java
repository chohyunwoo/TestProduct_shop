package org.example.domain.tag;

import org.example.domain.product.Category;
import org.example.domain.product.ProductId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Tag createPendingTag() {
        return Tag.createPending(
                ProductId.of("product-1"),
                "wireless",
                ConfidenceScore.of(0.85),
                Category.ELECTRONICS
        );
    }

    @Test
    void createPending_succeeds() {
        Tag tag = createPendingTag();

        assertNotNull(tag.id());
        assertEquals("wireless", tag.name());
        assertEquals(TagStatus.PENDING, tag.status());
        assertEquals(0.85, tag.confidence().value());
        assertEquals(ProductId.of("product-1"), tag.productId());
        assertEquals(Category.ELECTRONICS, tag.suggestedCategory());
        assertNull(tag.rejectionReason());
    }

    @Test
    void approve_fromPending_succeeds() {
        Tag tag = createPendingTag();
        tag.approve();

        assertEquals(TagStatus.APPROVED, tag.status());
    }

    @Test
    void reject_fromPending_succeeds() {
        Tag tag = createPendingTag();
        tag.reject("low confidence");

        assertEquals(TagStatus.REJECTED, tag.status());
        assertEquals("low confidence", tag.rejectionReason());
    }

    @Test
    void approve_fromApproved_throwsException() {
        Tag tag = createPendingTag();
        tag.approve();

        assertThrows(IllegalStateException.class, tag::approve);
    }

    @Test
    void reject_fromApproved_throwsException() {
        Tag tag = createPendingTag();
        tag.approve();

        assertThrows(IllegalStateException.class, () -> tag.reject("reason"));
    }

    @Test
    void approve_fromRejected_throwsException() {
        Tag tag = createPendingTag();
        tag.reject("reason");

        assertThrows(IllegalStateException.class, tag::approve);
    }

    @Test
    void reject_fromRejected_throwsException() {
        Tag tag = createPendingTag();
        tag.reject("reason");

        assertThrows(IllegalStateException.class, () -> tag.reject("another reason"));
    }

    @Test
    void reject_withNullReason_throwsException() {
        Tag tag = createPendingTag();
        assertThrows(IllegalArgumentException.class, () -> tag.reject(null));
    }

    @Test
    void reject_withBlankReason_throwsException() {
        Tag tag = createPendingTag();
        assertThrows(IllegalArgumentException.class, () -> tag.reject("  "));
    }

    @Test
    void isPending_returnsCorrectly() {
        Tag tag = createPendingTag();
        assertTrue(tag.isPending());
        assertFalse(tag.isApproved());

        tag.approve();
        assertFalse(tag.isPending());
        assertTrue(tag.isApproved());
    }
}
