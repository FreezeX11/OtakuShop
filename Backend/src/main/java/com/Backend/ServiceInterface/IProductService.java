package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.ProductRequest;
import com.Backend.Payload.Request.ProductSkuRequest;

import java.io.IOException;

public interface IProductService {
    void addProduct(ProductRequest productRequest) throws IOException;
    void updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
}
