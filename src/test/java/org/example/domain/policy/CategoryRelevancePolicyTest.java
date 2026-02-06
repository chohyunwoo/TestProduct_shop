package org.example.domain.policy;

import org.example.domain.product.Category;
import org.example.domain.product.Product;
import org.example.domain.product.ProductId;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRelevancePolicyTest {

    private final CategoryRelevancePolicy policy = new CategoryRelevancePolicy(
            Map.of(
                    Category.ELECTRONICS, Set.of("wireless", "bluetooth", "usb", "battery"),
                    Category.FASHION, Set.of("cotton", "leather", "silk", "denim"),
                    Category.FOOD, Set.of("organic", "fresh", "frozen", "vegan")
            )
    );

    private Tag tagWithNameAndCategory(String name, Category category) {
        return Tag.createPending(ProductId.of("p-1"), name, ConfidenceScore.of(0.9), category);
    }

    @Test
    void evaluate_relevantTag_approved() {
        Tag tag = tagWithNameAndCategory("wireless", Category.ELECTRONICS);
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isApproved());
    }

    @Test
    void evaluate_irrelevantTag_rejected() {
        Tag tag = tagWithNameAndCategory("cotton", Category.ELECTRONICS);
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isRejected());
        assertTrue(result.reason().contains("category"));
    }

    @Test
    void evaluate_unknownTag_approved() {
        // Tags not in any category keyword set are allowed (not all tags can be predefined)
        Tag tag = tagWithNameAndCategory("premium", Category.ELECTRONICS);
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isApproved());
    }

    @Test
    void evaluate_unmappedCategory_approved() {
        Tag tag = tagWithNameAndCategory("anything", Category.BOOKS);
        PolicyResult result = policy.evaluate(tag, List.of());
        assertTrue(result.isApproved());
    }
}
