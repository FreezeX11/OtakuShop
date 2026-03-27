package com.Backend.Mapper;

import com.Backend.Entity.ProductSku;
import com.Backend.Payload.Request.ProductSkuRequest;
import com.Backend.Payload.Response.ProductSkuResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ProductSkuMapper {
    private final VariationValueMapper variationValueMapper;
    private final ImageMapper imageMapper;

    public ProductSku toProductSku(ProductSkuRequest productSkuRequest) {
        ProductSku productSku = new ProductSku();

        productSku.setQuantity(productSkuRequest.getQuantity());
        productSku.setOutOfStock(productSkuRequest.getQuantity() == 0);
        productSku.setCreatedDate(LocalDateTime.now());

        return productSku;
    }

    public ProductSkuResponse toProductSkuResponse(ProductSku productSku) {
        ProductSkuResponse productSkuResponse = new ProductSkuResponse();

        productSkuResponse.setId(productSku.getId());
        productSkuResponse.setSku(productSku.getSku());
        productSkuResponse.setQuantity(productSku.getQuantity());
        productSkuResponse.setOutOfStock(productSku.isOutOfStock());
        productSkuResponse.setEnable(productSku.isEnable());

        productSkuResponse.setVariationValueResponses(
                productSku.getVariationValues().stream()
                        .map(variationValueMapper::toVariationValueResponse)
                        .toList()
        );

        productSkuResponse.setImageResponses(
                productSku.getImages().stream()
                        .map(imageMapper::toImageResponse)
                        .toList()
        );

        return productSkuResponse;
    }
}
