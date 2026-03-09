package com.Backend.Service;

import com.Backend.Entity.Image;
import com.Backend.Entity.Product;
import com.Backend.Entity.ProductSku;
import com.Backend.Entity.VariationValue;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.ProductSkuMapper;
import com.Backend.Payload.Request.ProductSkuRequest;
import com.Backend.Repository.ProductRepository;
import com.Backend.Repository.ProductSkuRepository;
import com.Backend.Repository.VariationValueRepository;
import com.Backend.ServiceInterface.IProductSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductSkuService implements IProductSkuService {
    private final ProductSkuRepository productSkuRepository;
    private final ProductRepository productRepository;
    private final ProductSkuMapper productSkuMapper;
    private final VariationValueRepository variationValueRepository;
    private final FileService fileService;
    @Value("${upload.path}")
    private String imageUploadPath;

    @Override
    public ProductSku addProductSku(ProductSkuRequest productSkuRequest) {
        ProductSku productSku = productSkuMapper.toProductSku(productSkuRequest);
        List<VariationValue> variationValues = variationValueRepository.findAllById(productSkuRequest.getVariationValuesIds());
        String sku = "Sku_" + UUID.randomUUID().toString();

        productSkuRequest.getFiles()
                .forEach(file -> {
                    try {
                        String fileName = fileService.uploadFile(file, imageUploadPath);
                        Image image = new Image();
                        image.setUrl(fileName);
                        image.setCreatedDate(LocalDateTime.now());
                        productSku.getImages().add(image);
                    } catch (IOException e) {
                        throw new RuntimeException("Error uploading product image", e);
                    }
                });

        productSku.setVariationValues(variationValues);
        productSku.setSku(sku);

        return productSku;
    }

    @Override
    public void addProductSku(Long productId, ProductSkuRequest productSkuRequest) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("This variant doesn't exist"));

        if(existingProduct.getProductSkus().size() == 1
                && existingProduct.getProductSkus().getFirst().getVariationValues().isEmpty()) {
            Long productSkuId = existingProduct.getProductSkus().getFirst().getId();

            productSkuRepository.deleteById(productSkuId);
        }

        ProductSku productSku = productSkuMapper.toProductSku(productSkuRequest);
        List<VariationValue> variationValues = variationValueRepository.findAllById(productSkuRequest.getVariationValuesIds());

        String sku = "Sku_" + UUID.randomUUID().toString();

        productSkuRequest.getFiles()
                .forEach(file -> {
                    try {
                        String fileName = fileService.uploadFile(file, imageUploadPath);
                        Image image = new Image();
                        image.setUrl(fileName);
                        image.setCreatedDate(LocalDateTime.now());
                        productSku.getImages().add(image);
                    } catch (IOException e) {
                        throw new RuntimeException("Error uploading product image", e);
                    }
                });

        productSku.setVariationValues(variationValues);
        productSku.setSku(sku);
        productSku.setProduct(existingProduct);

        productSkuRepository.save(productSku);
    }

    @Override
    public void updateProductSku(
            Long productId,
            Long productSkuId,
            ProductSkuRequest productSkuRequest
    ) {
        ProductSku existingProductSku = productSkuRepository.findById(productSkuId)
                .orElseThrow(() -> new ResourceNotFoundException("This variant doesn't exist"));

        if(!existingProductSku.getProduct().getId().equals(productId)){
            throw new IllegalArgumentException("SKU does not belong to this product");
        }

        List<Image> images = new ArrayList<>();

        productSkuRequest.getFiles()
                .forEach(file -> {
                    try {
                        String fileName = fileService.uploadFile(file, imageUploadPath);
                        Image image = new Image();
                        image.setUrl(fileName);
                        image.setCreatedDate(LocalDateTime.now());
                        images.add(image);
                    } catch (IOException e) {
                        throw new RuntimeException("Error uploading product image", e);
                    }
                });

        existingProductSku.setQuantity(productSkuRequest.getQuantity());
        existingProductSku.setImages(images);
        existingProductSku.setOutOfStock(productSkuRequest.getQuantity() == 0);
        productSkuRepository.save(existingProductSku);
    }

    @Override
    public void enableProductSKu(Long productId, Long productSkuId) {
        ProductSku productSku = productSkuRepository.findById(productSkuId)
                .orElseThrow(() -> new ResourceNotFoundException("This variant doesn't exist"));

        productSku.setEnable(true);
        productSkuRepository.save(productSku);
    }

    @Override
    public void disableProductSku(Long productId, Long productSkuId) {
        ProductSku productSku = productSkuRepository.findById(productSkuId)
                .orElseThrow(() -> new ResourceNotFoundException("This variant doesn't exist"));

        productSku.setEnable(false);
        productSkuRepository.save(productSku);
    }
}
