package org.example.domain.tag;

import org.example.domain.product.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiTagSuggestionTest {

    @Test
    void create_withValidArgs_succeeds() {
        ConfidenceScore score = ConfidenceScore.of(0.9);
        AiTagSuggestion suggestion = new AiTagSuggestion("wireless", score, Category.ELECTRONICS);

        assertEquals("wireless", suggestion.tagName());
        assertEquals(score, suggestion.confidence());
        assertEquals(Category.ELECTRONICS, suggestion.suggestedCategory());
    }

    @Test
    void create_withNullName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AiTagSuggestion(null, ConfidenceScore.of(0.5), Category.ELECTRONICS));
    }

    @Test
    void create_withBlankName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AiTagSuggestion("  ", ConfidenceScore.of(0.5), Category.ELECTRONICS));
    }

    @Test
    void create_withNullConfidence_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AiTagSuggestion("wireless", null, Category.ELECTRONICS));
    }

    @Test
    void create_withNullCategory_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AiTagSuggestion("wireless", ConfidenceScore.of(0.5), null));
    }

    @Test
    void tagName_isTrimmedAndLowerCased() {
        AiTagSuggestion suggestion = new AiTagSuggestion("  Wireless  ", ConfidenceScore.of(0.9), Category.ELECTRONICS);
        assertEquals("wireless", suggestion.tagName());
    }
}
