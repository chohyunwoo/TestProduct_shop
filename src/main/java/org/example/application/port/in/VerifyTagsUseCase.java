package org.example.application.port.in;

import org.example.domain.product.ProductId;

public interface VerifyTagsUseCase {

    void verifyTags(ProductId productId);
}
