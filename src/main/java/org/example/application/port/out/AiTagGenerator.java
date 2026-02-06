package org.example.application.port.out;

import org.example.domain.product.Product;
import org.example.domain.tag.AiTagSuggestion;

import java.util.List;

public interface AiTagGenerator {

    List<AiTagSuggestion> suggestTags(Product product);
}
