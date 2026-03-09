package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.ProductRequest;
import com.Backend.Payload.Request.ProductSkuRequest;
import com.Backend.Payload.Response.ProductResponse;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    void addProduct(ProductRequest productRequest) throws IOException;
    void updateProduct(Long id, ProductRequest productRequest) throws IOException;
    void enableProduct(Long id);
    void disableProduct(Long id);
    ProductResponse getProduct(Long id);
    List<ProductResponse> getProducts();
}
