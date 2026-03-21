package com.Backend.Service;

import com.Backend.Entity.Product;
import com.Backend.Entity.ProductSku;
import com.Backend.Entity.SubCategory;
import com.Backend.Entity.Tag;
import com.Backend.Exception.BusinessException;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.ProductMapper;
import com.Backend.Payload.Request.ProductRequest;
import com.Backend.Payload.Response.ProductResponse;
import com.Backend.Repository.ProductRepository;
import com.Backend.Repository.ProductSkuRepository;
import com.Backend.Repository.SubCategoryRepository;
import com.Backend.Repository.TagRepository;
import com.Backend.ServiceInterface.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;
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

        productRequest.getProductSkuRequests()
                .forEach(productSkuRequest -> {
                    ProductSku productSku = productSkuService.addProductSku(productSkuRequest);
                    productSku.setProduct(mappedProduct);
                    mappedProduct.getProductSkus().add(productSku);
                });

        productRepository.save(mappedProduct);
    }

    @Override
    public void updateProduct(Long id, ProductRequest productRequest) throws IOException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This product doesn't exist"));

        SubCategory existingSubCategory = subCategoryRepository.findById(productRequest.getSubCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("This subcategory doesn't exist"));

        List<Tag> tags = tagRepository.findAllById(productRequest.getTagsIds());

        if(productRepository.findByNameIgnoreCase(productRequest.getName().trim()).isPresent()
                && !Objects.equals(existingProduct.getName(), productRequest.getName())) {
            throw new ResourceAlreadyExistException("This product already exist");
        }

        if(existingProduct.getProductSkus().size() == 1
                && existingProduct.getProductSkus().getFirst().getVariationValues().isEmpty()) {

            ProductSku existingProductSku = existingProduct.getProductSkus().getFirst();
            int quantity = productRequest.getProductSkuRequests().getFirst().getQuantity();

            existingProductSku.setQuantity(quantity);
            productSkuRepository.save(existingProductSku);
        }

        if(productRequest.getCover() != null) {
            String coverPath = fileService.uploadFile(productRequest.getCover(), imageUploadPath);
            existingProduct.setCover(coverPath);
        }

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setSubCategory(existingSubCategory);
        existingProduct.setTags(tags);

        productRepository.save(existingProduct);
    }

    @Override
    public void enableProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This product doesn't exist"));

        existingProduct.getProductSkus()
                .forEach(productSku -> productSkuService.enableProductSku(id, productSku.getId()));

        existingProduct.setEnable(true);

        productRepository.save(existingProduct);
    }

    @Override
    public void disableProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This product doesn't exist"));

        existingProduct.getProductSkus()
                .forEach(productSku -> productSkuService.disableProductSku(id, productSku.getId()));

        existingProduct.setEnable(false);

        productRepository.save(existingProduct);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This product doesn't exist"));

        return productMapper.toProductResponse(existingProduct);
    }

    @Override
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
