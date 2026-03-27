package com.Backend.Mapper;

import com.Backend.Entity.Product;
import com.Backend.Payload.Request.ProductRequest;
import com.Backend.Payload.Response.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ProductMapper {
    private final SubCategoryMapper subCategoryMapper;
    private final TagMapper tagMapper;
    private final ProductSkuMapper productSkuMapper;

    public Product toProduct(ProductRequest productRequest) {
        Product product = new Product();

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCreatedDate(LocalDateTime.now());

        return product;
    }

    public ProductResponse toProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setCover(product.getCover());
        productResponse.setPrice(product.getPrice());
        productResponse.setEnable(product.isEnable());
        productResponse.setSubCategoryResponse(
                subCategoryMapper.toSubCategoryResponse(product.getSubCategory())
        );

        productResponse.setTagResponses(
                product.getTags().stream()
                        .map(tagMapper::toTagResponse)
                        .toList()
        );

        productResponse.setProductSkuResponses(
                product.getProductSkus().stream()
                        .map(productSkuMapper::toProductSkuResponse)
                        .toList()
        );

        return productResponse;
    }
}
