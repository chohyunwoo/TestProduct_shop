package org.example.application.port.in;

import org.example.domain.product.ProductId;
import org.example.domain.tag.Tag;

import java.util.List;

public interface RequestTagSuggestionUseCase {

    List<Tag> requestSuggestions(ProductId productId);
}
