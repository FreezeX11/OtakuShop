package com.Backend.Service;

import com.Backend.Entity.Image;
import com.Backend.Entity.Product;
import com.Backend.Entity.ProductSku;
import com.Backend.Entity.VariationValue;
import com.Backend.Exception.BusinessException;
import com.Backend.Exception.FileUploadException;
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
import org.springframework.transaction.annotation.Transactional;

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
                        throw new FileUploadException("Error uploading product image", e);
                    }
                });

        productSku.setVariationValues(variationValues);
        productSku.setSku(sku);

        return productSku;
    }

    @Transactional
    @Override
    public void addProductSku(Long productId, ProductSkuRequest productSkuRequest) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("This variant doesn't exist"));

        if(existingProduct.getProductSkus().size() == 1
                && existingProduct.getProductSkus().getFirst().getVariationValues().isEmpty()) {
            ProductSku existingProductSku = existingProduct.getProductSkus().getFirst();

            existingProduct.getProductSkus().remove(existingProductSku);
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
                        throw new FileUploadException("Error uploading product image", e);
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
    ) throws IOException {
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
                        throw new FileUploadException("Error uploading product image", e);
                    }
                });

        existingProductSku.setQuantity(productSkuRequest.getQuantity());
        existingProductSku.setOutOfStock(productSkuRequest.getQuantity() == 0);

        List<Image> productSkuImages = existingProductSku.getImages();
        for(Image image : productSkuImages) {
            fileService.deleteFile(image.getUrl(), imageUploadPath);
        }

        existingProductSku.getImages().clear();
        existingProductSku.getImages().addAll(images);

        productSkuRepository.save(existingProductSku);
    }

    @Override
    public void enableProductSku(Long productId, Long productSkuId) {
        ProductSku existingProductSku = productSkuRepository.findById(productSkuId)
                .orElseThrow(() -> new ResourceNotFoundException("This variant doesn't exist"));

        if(!existingProductSku.getProduct().getId().equals(productId)){
            throw new IllegalArgumentException("SKU does not belong to this product");
        }

        existingProductSku.setEnable(true);
        productSkuRepository.save(existingProductSku);
    }

    @Override
    public void disableProductSku(Long productId, Long productSkuId) {
        ProductSku existingProductSku = productSkuRepository.findById(productSkuId)
                .orElseThrow(() -> new ResourceNotFoundException("This variant doesn't exist"));

        if(!existingProductSku.getProduct().getId().equals(productId)){
            throw new BusinessException("SKU does not belong to this product");
        }

        existingProductSku.setEnable(false);
        productSkuRepository.save(existingProductSku);
    }
}
