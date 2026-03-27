package com.Backend.ServiceInterface;

import com.Backend.Entity.ProductSku;
import com.Backend.Payload.Request.ProductSkuRequest;

import java.io.IOException;

public interface IProductSkuService {
    ProductSku addProductSku(ProductSkuRequest productSkuRequest);
    void addProductSku(Long productId, ProductSkuRequest productSkuRequest);
    void updateProductSku(Long productId, Long productSkuId, ProductSkuRequest productSkuRequest) throws IOException;
    void enableProductSku(Long productId, Long productSkuId);
    void disableProductSku(Long productId, Long productSkuId);
}
