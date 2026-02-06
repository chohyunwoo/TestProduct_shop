package org.example.web;

import org.example.application.port.in.GetApprovedTagsUseCase;
import org.example.application.port.in.RequestTagSuggestionUseCase;
import org.example.application.port.in.VerifyTagsUseCase;
import org.example.domain.product.Product;
import org.example.domain.product.ProductId;
import org.example.domain.product.ProductRepository;
import org.example.domain.tag.Tag;
import org.example.web.dto.CreateProductRequest;
import org.example.web.dto.ProductResponse;
import org.example.web.dto.TagResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductTagController {

    private final ProductRepository productRepository;
    private final RequestTagSuggestionUseCase requestTagSuggestionUseCase;
    private final VerifyTagsUseCase verifyTagsUseCase;
    private final GetApprovedTagsUseCase getApprovedTagsUseCase;

    public ProductTagController(ProductRepository productRepository,
                                RequestTagSuggestionUseCase requestTagSuggestionUseCase,
                                VerifyTagsUseCase verifyTagsUseCase,
                                GetApprovedTagsUseCase getApprovedTagsUseCase) {
        this.productRepository = productRepository;
        this.requestTagSuggestionUseCase = requestTagSuggestionUseCase;
        this.verifyTagsUseCase = verifyTagsUseCase;
        this.getApprovedTagsUseCase = getApprovedTagsUseCase;
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody CreateProductRequest request) {
        Product product = Product.create(request.name(), request.description(), request.category());
        productRepository.save(product);
        return ProductResponse.from(product);
    }

    @PostMapping("/{productId}/tags/suggest")
    public List<TagResponse> suggestTags(@PathVariable String productId) {
        List<Tag> tags = requestTagSuggestionUseCase.requestSuggestions(ProductId.of(productId));
        return tags.stream().map(TagResponse::from).toList();
    }

    @PostMapping("/{productId}/tags/verify")
    public ResponseEntity<Void> verifyTags(@PathVariable String productId) {
        verifyTagsUseCase.verifyTags(ProductId.of(productId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/tags/approved")
    public List<TagResponse> getApprovedTags(@PathVariable String productId) {
        List<Tag> tags = getApprovedTagsUseCase.getApprovedTags(ProductId.of(productId));
        return tags.stream().map(TagResponse::from).toList();
    }
}
