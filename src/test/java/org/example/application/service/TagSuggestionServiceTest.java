package org.example.application.service;

import org.example.domain.product.Category;
import org.example.domain.product.Product;
import org.example.domain.product.ProductId;
import org.example.domain.tag.AiTagSuggestion;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.Tag;
import org.example.domain.tag.TagStatus;
import org.example.infrastructure.ai.StubAiTagGenerator;
import org.example.infrastructure.persistence.InMemoryProductRepository;
import org.example.infrastructure.persistence.InMemoryTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagSuggestionServiceTest {

    private InMemoryProductRepository productRepository;
    private InMemoryTagRepository tagRepository;
    private StubAiTagGenerator aiTagGenerator;
    private TagSuggestionService service;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        tagRepository = new InMemoryTagRepository();
        aiTagGenerator = new StubAiTagGenerator(List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("bluetooth", ConfidenceScore.of(0.7), Category.ELECTRONICS)
        ));
        service = new TagSuggestionService(productRepository, tagRepository, aiTagGenerator);
    }

    @Test
    void requestSuggestions_createsTagsAsPending() {
        Product product = Product.create("이어폰", "블루투스 이어폰", Category.ELECTRONICS);
        productRepository.save(product);

        List<Tag> tags = service.requestSuggestions(product.id());

        assertEquals(2, tags.size());
        assertTrue(tags.stream().allMatch(t -> t.status() == TagStatus.PENDING));
        assertEquals("wireless", tags.get(0).name());
        assertEquals("bluetooth", tags.get(1).name());
    }

    @Test
    void requestSuggestions_savesTagsToRepository() {
        Product product = Product.create("이어폰", "설명", Category.ELECTRONICS);
        productRepository.save(product);

        service.requestSuggestions(product.id());

        List<Tag> stored = tagRepository.findByProductId(product.id());
        assertEquals(2, stored.size());
    }

    @Test
    void requestSuggestions_productNotFound_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.requestSuggestions(ProductId.of("nonexistent")));
    }
}
