package com.Backend.Mapper;

import com.Backend.Entity.ProductSku;
import com.Backend.Payload.Request.ProductSkuRequest;
import com.Backend.Payload.Response.ProductSkuResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductSkuMapper {

    public ProductSku toProductSku(ProductSkuRequest productSkuRequest) {
        ProductSku productSku = new ProductSku();

        productSku.setQuantity(productSkuRequest.getQuantity());
        productSku.setOutOfStock(productSkuRequest.getQuantity() == 0);
        productSku.setCreatedDate(LocalDateTime.now());

        return productSku;
    }

    public ProductSkuResponse toProductSkuResponse(ProductSku productSku) {
        return null;
    }
}
