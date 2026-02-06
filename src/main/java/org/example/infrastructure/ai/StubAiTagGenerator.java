package org.example.infrastructure.ai;

import org.example.application.port.out.AiTagGenerator;
import org.example.domain.product.Product;
import org.example.domain.tag.AiTagSuggestion;

import java.util.List;

public class StubAiTagGenerator implements AiTagGenerator {

    private final List<AiTagSuggestion> stubbedSuggestions;

    public StubAiTagGenerator(List<AiTagSuggestion> stubbedSuggestions) {
        this.stubbedSuggestions = stubbedSuggestions;
    }

    @Override
    public List<AiTagSuggestion> suggestTags(Product product) {
        return stubbedSuggestions;
    }
}
