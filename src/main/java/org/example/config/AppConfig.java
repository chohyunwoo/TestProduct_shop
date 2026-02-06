package org.example.config;

import org.example.application.port.out.AiTagGenerator;
import org.example.application.service.ApprovedTagQueryService;
import org.example.application.service.TagSuggestionService;
import org.example.application.service.TagVerificationPipeline;
import org.example.application.service.TagVerificationService;
import org.example.domain.policy.CategoryRelevancePolicy;
import org.example.domain.policy.DuplicateTagPolicy;
import org.example.domain.policy.MaxTagCountPolicy;
import org.example.domain.policy.MinConfidencePolicy;
import org.example.domain.policy.ProfanityPolicy;
import org.example.domain.policy.TagPolicy;
import org.example.domain.product.Category;
import org.example.domain.product.ProductRepository;
import org.example.domain.tag.AiTagSuggestion;
import org.example.domain.tag.ConfidenceScore;
import org.example.domain.tag.TagRepository;
import org.example.infrastructure.ai.StubAiTagGenerator;
import org.example.infrastructure.persistence.InMemoryProductRepository;
import org.example.infrastructure.persistence.InMemoryTagRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ProductRepository productRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public TagRepository tagRepository() {
        return new InMemoryTagRepository();
    }

    @Bean
    public AiTagGenerator aiTagGenerator() {
        return new StubAiTagGenerator(defaultSuggestions());
    }

    @Bean
    public TagSuggestionService tagSuggestionService(ProductRepository productRepository,
                                                     TagRepository tagRepository,
                                                     AiTagGenerator aiTagGenerator) {
        return new TagSuggestionService(productRepository, tagRepository, aiTagGenerator);
    }

    @Bean
    public TagVerificationPipeline tagVerificationPipeline() {
        List<TagPolicy> policies = List.of(
                new DuplicateTagPolicy(),
                new MaxTagCountPolicy(5),
                new MinConfidencePolicy(0.6),
                new ProfanityPolicy(Set.of("badword", "xxx")),
                new CategoryRelevancePolicy(Map.of(
                        Category.ELECTRONICS, Set.of("phone", "laptop", "tablet", "camera"),
                        Category.FASHION, Set.of("shirt", "jeans", "sneakers", "jacket"),
                        Category.FOOD, Set.of("snack", "coffee", "tea", "chocolate"),
                        Category.BOOKS, Set.of("novel", "textbook", "comic", "magazine")
                ))
        );
        return new TagVerificationPipeline(policies);
    }

    @Bean
    public TagVerificationService tagVerificationService(TagRepository tagRepository,
                                                         TagVerificationPipeline tagVerificationPipeline) {
        return new TagVerificationService(tagRepository, tagVerificationPipeline);
    }

    @Bean
    public ApprovedTagQueryService approvedTagQueryService(TagRepository tagRepository) {
        return new ApprovedTagQueryService(tagRepository);
    }

    private List<AiTagSuggestion> defaultSuggestions() {
        return List.of(
                new AiTagSuggestion("phone", ConfidenceScore.of(0.91), Category.ELECTRONICS),
                new AiTagSuggestion("laptop", ConfidenceScore.of(0.87), Category.ELECTRONICS),
                new AiTagSuggestion("camera", ConfidenceScore.of(0.62), Category.ELECTRONICS),
                new AiTagSuggestion("jacket", ConfidenceScore.of(0.58), Category.FASHION),
                new AiTagSuggestion("novel", ConfidenceScore.of(0.72), Category.BOOKS)
        );
    }
}
