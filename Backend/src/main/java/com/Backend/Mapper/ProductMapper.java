package com.Backend.Mapper;

import com.Backend.Entity.Product;
import com.Backend.Payload.Request.ProductRequest;
import com.Backend.Payload.Response.ProductResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductMapper {

    public Product toProduct(ProductRequest productRequest) {
        Product product = new Product();

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCreatedDate(LocalDateTime.now());

        return product;
    }

    public ProductResponse toProductResponse() {return null;}
}
