package org.example.infrastructure.ai;

import org.example.domain.product.Category;
import org.example.domain.product.Product;
import org.example.domain.tag.AiTagSuggestion;
import org.example.domain.tag.ConfidenceScore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StubAiTagGeneratorTest {

    @Test
    void suggestTags_returnsConfiguredSuggestions() {
        List<AiTagSuggestion> stubSuggestions = List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("bluetooth", ConfidenceScore.of(0.8), Category.ELECTRONICS)
        );
        StubAiTagGenerator generator = new StubAiTagGenerator(stubSuggestions);

        Product product = Product.create("이어폰", "블루투스 이어폰", Category.ELECTRONICS);
        List<AiTagSuggestion> result = generator.suggestTags(product);

        assertEquals(2, result.size());
        assertEquals("wireless", result.get(0).tagName());
        assertEquals("bluetooth", result.get(1).tagName());
    }

    @Test
    void suggestTags_emptySuggestions_returnsEmpty() {
        StubAiTagGenerator generator = new StubAiTagGenerator(List.of());

        Product product = Product.create("이어폰", "설명", Category.ELECTRONICS);
        List<AiTagSuggestion> result = generator.suggestTags(product);

        assertTrue(result.isEmpty());
    }
}
