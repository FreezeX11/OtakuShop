package com.Backend.ServiceInterface;

import com.Backend.Entity.Product;
import com.Backend.Payload.Request.ProductSkuRequest;

import java.util.List;

public interface IProductSkuService {
    void addProductSku(ProductSkuRequest productSkuRequest, Product product);
}
