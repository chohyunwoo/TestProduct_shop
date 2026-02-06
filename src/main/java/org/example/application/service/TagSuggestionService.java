package org.example.application.service;

import org.example.application.port.in.RequestTagSuggestionUseCase;
import org.example.application.port.out.AiTagGenerator;
import org.example.domain.product.Product;
import org.example.domain.product.ProductId;
import org.example.domain.product.ProductRepository;
import org.example.domain.tag.AiTagSuggestion;
import org.example.domain.tag.Tag;
import org.example.domain.tag.TagRepository;

import java.util.ArrayList;
import java.util.List;

public class TagSuggestionService implements RequestTagSuggestionUseCase {

    private final ProductRepository productRepository;
    private final TagRepository tagRepository;
    private final AiTagGenerator aiTagGenerator;

    public TagSuggestionService(ProductRepository productRepository, TagRepository tagRepository, AiTagGenerator aiTagGenerator) {
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
        this.aiTagGenerator = aiTagGenerator;
    }

    @Override
    public List<Tag> requestSuggestions(ProductId productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        List<AiTagSuggestion> suggestions = aiTagGenerator.suggestTags(product);

        List<Tag> tags = new ArrayList<>();
        for (AiTagSuggestion suggestion : suggestions) {
            Tag tag = Tag.createPending(
                    productId,
                    suggestion.tagName(),
                    suggestion.confidence(),
                    suggestion.suggestedCategory()
            );
            tagRepository.save(tag);
            tags.add(tag);
        }
        return tags;
    }
}
