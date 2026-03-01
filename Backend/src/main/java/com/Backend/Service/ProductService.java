package com.Backend.Service;

import com.Backend.Entity.Product;
import com.Backend.Entity.SubCategory;
import com.Backend.Entity.Tag;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.ProductMapper;
import com.Backend.Payload.Request.ProductRequest;
import com.Backend.Payload.Request.ProductSkuRequest;
import com.Backend.Repository.ProductRepository;
import com.Backend.Repository.SubCategoryRepository;
import com.Backend.Repository.TagRepository;
import com.Backend.ServiceInterface.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
@AllArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TagRepository tagRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;
    private final ProductSkuService productSkuService;
    @Value("${upload.path}")
    private String imageUploadPath;

    @Override
    public void addProduct(ProductRequest productRequest) throws IOException {
        if (productRepository.findByNameIgnoreCase(productRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This product already exist");

        SubCategory subCategory = subCategoryRepository.findById(productRequest.getSubCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("This subcategory doesn't exist"));

        List<Tag> tags = tagRepository.findAllById(productRequest.getTagsIds());

        Product mappedProduct = productMapper.toProduct(productRequest);
        String coverPath = fileService.uploadFile(productRequest.getCover(), imageUploadPath);

        mappedProduct.setSubCategory(subCategory);
        mappedProduct.setCover(coverPath);
        mappedProduct.setTags(tags);

        Product product = productRepository.save(mappedProduct);

        productRequest.getProductSkuRequests()
                .forEach(productSkuRequest -> productSkuService.addProductSku(productSkuRequest, product));
    }

    @Override
    public void updateProduct(Long id, ProductRequest productRequest) {

    }

    @Override
    public void deleteProduct(Long id) {

    }
}
