package com.Backend.Service;

import com.Backend.Entity.Image;
import com.Backend.Entity.Product;
import com.Backend.Entity.ProductSku;
import com.Backend.Entity.VariationValue;
import com.Backend.Mapper.ProductSkuMapper;
import com.Backend.Payload.Request.ProductSkuRequest;
import com.Backend.Repository.ProductSkuRepository;
import com.Backend.Repository.VariationValueRepository;
import com.Backend.ServiceInterface.IProductSkuService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductSkuService implements IProductSkuService {
    private final ProductSkuRepository productSkuRepository;
    private final ProductSkuMapper productSkuMapper;
    private final VariationValueRepository variationValueRepository;
    private final FileService fileService;
    @Value("${upload.path}")
    private String imageUploadPath;

    @Override
    public void addProductSku(ProductSkuRequest productSkuRequest, Product product) {
        ProductSku productSku = productSkuMapper.toProductSku(productSkuRequest);
        List<VariationValue> variationValues = variationValueRepository.findAllById(productSkuRequest.getVariationValuesIds());
        String sku = "Sku" + UUID.randomUUID().toString();

        productSkuRequest.getFiles()
                .forEach(file -> {
                    try {
                        String fileName = fileService.uploadFile(file, imageUploadPath);
                        Image image = new Image();
                        image.setUrl(fileName);
                        productSku.getImages().add(image);
                    } catch (IOException e) {
                        throw new RuntimeException("Error uploading product image", e);
                    }
                });

        productSku.setVariationValues(variationValues);
        productSku.setSku(sku);
        productSku.setProduct(product);

        productSkuRepository.save(productSku);
    }
}
