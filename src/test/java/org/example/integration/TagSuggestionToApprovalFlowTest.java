package org.example.integration;

import org.example.application.service.ApprovedTagQueryService;
import org.example.application.service.TagSuggestionService;
import org.example.application.service.TagVerificationPipeline;
import org.example.application.service.TagVerificationService;
import org.example.domain.policy.*;
import org.example.domain.product.Category;
import org.example.domain.product.Product;
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
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TagSuggestionToApprovalFlowTest {

    private InMemoryProductRepository productRepository;
    private InMemoryTagRepository tagRepository;
    private TagSuggestionService suggestionService;
    private TagVerificationService verificationService;
    private ApprovedTagQueryService queryService;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        tagRepository = new InMemoryTagRepository();

        TagVerificationPipeline pipeline = new TagVerificationPipeline(List.of(
                new MinConfidencePolicy(0.5),
                new ProfanityPolicy(Set.of("badword", "offensive")),
                new DuplicateTagPolicy(),
                new MaxTagCountPolicy(5),
                new CategoryRelevancePolicy(Map.of(
                        Category.ELECTRONICS, Set.of("wireless", "bluetooth", "usb", "battery", "charger"),
                        Category.FASHION, Set.of("cotton", "leather", "silk", "denim")
                ))
        ));

        verificationService = new TagVerificationService(tagRepository, pipeline);
        queryService = new ApprovedTagQueryService(tagRepository);
    }

    private void setupSuggestionService(List<AiTagSuggestion> suggestions) {
        StubAiTagGenerator generator = new StubAiTagGenerator(suggestions);
        suggestionService = new TagSuggestionService(productRepository, tagRepository, generator);
    }

    @Test
    void fullFlow_productCreation_toApprovedTags() {
        // 1. Product creation
        Product product = Product.create("무선 이어폰", "고품질 블루투스 이어폰", Category.ELECTRONICS);
        productRepository.save(product);

        // 2. AI suggests tags
        setupSuggestionService(List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("bluetooth", ConfidenceScore.of(0.85), Category.ELECTRONICS),
                new AiTagSuggestion("premium", ConfidenceScore.of(0.7), Category.ELECTRONICS)
        ));

        List<Tag> suggestedTags = suggestionService.requestSuggestions(product.id());

        // 3. All tags are PENDING
        assertEquals(3, suggestedTags.size());
        assertTrue(suggestedTags.stream().allMatch(t -> t.status() == TagStatus.PENDING));

        // 4. Verify tags
        verificationService.verifyTags(product.id());

        // 5. Query approved tags
        List<Tag> approvedTags = queryService.getApprovedTags(product.id());
        assertEquals(3, approvedTags.size());
    }

    @Test
    void fullFlow_lowConfidenceTag_rejected() {
        Product product = Product.create("이어폰", "설명", Category.ELECTRONICS);
        productRepository.save(product);

        setupSuggestionService(List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("cheap", ConfidenceScore.of(0.3), Category.ELECTRONICS)  // low confidence
        ));

        suggestionService.requestSuggestions(product.id());
        verificationService.verifyTags(product.id());

        List<Tag> approvedTags = queryService.getApprovedTags(product.id());
        assertEquals(1, approvedTags.size());
        assertEquals("wireless", approvedTags.get(0).name());

        // Verify rejected tag has reason
        List<Tag> allTags = tagRepository.findByProductId(product.id());
        Tag rejectedTag = allTags.stream()
                .filter(t -> t.status() == TagStatus.REJECTED)
                .findFirst()
                .orElseThrow();
        assertEquals("cheap", rejectedTag.name());
        assertTrue(rejectedTag.rejectionReason().contains("confidence"));
    }

    @Test
    void fullFlow_profanityTag_rejected() {
        Product product = Product.create("이어폰", "설명", Category.ELECTRONICS);
        productRepository.save(product);

        setupSuggestionService(List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("badword", ConfidenceScore.of(0.95), Category.ELECTRONICS)
        ));

        suggestionService.requestSuggestions(product.id());
        verificationService.verifyTags(product.id());

        List<Tag> approvedTags = queryService.getApprovedTags(product.id());
        assertEquals(1, approvedTags.size());
        assertEquals("wireless", approvedTags.get(0).name());
    }

    @Test
    void fullFlow_duplicateTag_rejected() {
        Product product = Product.create("이어폰", "설명", Category.ELECTRONICS);
        productRepository.save(product);

        setupSuggestionService(List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.85), Category.ELECTRONICS)  // duplicate
        ));

        suggestionService.requestSuggestions(product.id());
        verificationService.verifyTags(product.id());

        List<Tag> approvedTags = queryService.getApprovedTags(product.id());
        assertEquals(1, approvedTags.size());

        List<Tag> allTags = tagRepository.findByProductId(product.id());
        Tag rejected = allTags.stream()
                .filter(t -> t.status() == TagStatus.REJECTED)
                .findFirst()
                .orElseThrow();
        assertTrue(rejected.rejectionReason().contains("duplicate"));
    }

    @Test
    void fullFlow_maxTagCountExceeded_rejected() {
        Product product = Product.create("이어폰", "설명", Category.ELECTRONICS);
        productRepository.save(product);

        // MaxTagCountPolicy is 5, so 6th tag should be rejected
        setupSuggestionService(List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("bluetooth", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("premium", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("portable", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("lightweight", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("durable", ConfidenceScore.of(0.9), Category.ELECTRONICS)  // 6th - exceeds max
        ));

        suggestionService.requestSuggestions(product.id());
        verificationService.verifyTags(product.id());

        List<Tag> approvedTags = queryService.getApprovedTags(product.id());
        assertEquals(5, approvedTags.size());

        List<Tag> allTags = tagRepository.findByProductId(product.id());
        Tag rejected = allTags.stream()
                .filter(t -> t.status() == TagStatus.REJECTED)
                .findFirst()
                .orElseThrow();
        assertTrue(rejected.rejectionReason().contains("maximum"));
    }

    @Test
    void fullFlow_categoryIrrelevantTag_rejected() {
        Product product = Product.create("이어폰", "설명", Category.ELECTRONICS);
        productRepository.save(product);

        setupSuggestionService(List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),
                new AiTagSuggestion("cotton", ConfidenceScore.of(0.9), Category.ELECTRONICS)  // FASHION keyword on ELECTRONICS
        ));

        suggestionService.requestSuggestions(product.id());
        verificationService.verifyTags(product.id());

        List<Tag> approvedTags = queryService.getApprovedTags(product.id());
        assertEquals(1, approvedTags.size());
        assertEquals("wireless", approvedTags.get(0).name());
    }

    @Test
    void fullFlow_onlyApprovedTagsVisibleForSearch() {
        Product product = Product.create("이어폰", "설명", Category.ELECTRONICS);
        productRepository.save(product);

        setupSuggestionService(List.of(
                new AiTagSuggestion("wireless", ConfidenceScore.of(0.9), Category.ELECTRONICS),   // approved
                new AiTagSuggestion("badword", ConfidenceScore.of(0.95), Category.ELECTRONICS),   // rejected (profanity)
                new AiTagSuggestion("cheap", ConfidenceScore.of(0.3), Category.ELECTRONICS),      // rejected (low confidence)
                new AiTagSuggestion("bluetooth", ConfidenceScore.of(0.8), Category.ELECTRONICS)    // approved
        ));

        suggestionService.requestSuggestions(product.id());
        verificationService.verifyTags(product.id());

        // Only APPROVED tags are visible for search/display
        List<Tag> searchableTags = queryService.getApprovedTags(product.id());
        assertEquals(2, searchableTags.size());
        assertTrue(searchableTags.stream().anyMatch(t -> t.name().equals("wireless")));
        assertTrue(searchableTags.stream().anyMatch(t -> t.name().equals("bluetooth")));

        // Rejected tags exist but are not exposed
        List<Tag> allTags = tagRepository.findByProductId(product.id());
        assertEquals(4, allTags.size());
        long rejectedCount = allTags.stream().filter(t -> t.status() == TagStatus.REJECTED).count();
        assertEquals(2, rejectedCount);
    }
}
