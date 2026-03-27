package com.Backend.Controller;

import com.Backend.Payload.Request.ProductRequest;
import com.Backend.Payload.Request.ProductSkuRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.ProductService;
import com.Backend.Service.ProductSkuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ProductSkuService productSkuService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> addProduct(@Valid @ModelAttribute ProductRequest productRequest) throws IOException {
        productService.addProduct(productRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(
            value = "/{id}",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute  ProductRequest productRequest
    ) throws IOException {
        productService.updateProduct(id, productRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableProduct(@PathVariable Long id) {
        productService.enableProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProduct(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                productService.getProduct(id)
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProducts() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                productService.getProducts()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    //ProductSku
    @PostMapping(
            value = "/{id}/productSkus",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<Void> addProductSku(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductSkuRequest productSkuRequest
    ) {
        productSkuService.addProductSku(id, productSkuRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(
            value = "/{id}/productSkus/{productSkuId}",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ApiResponse> updateProductSku(
            @PathVariable Long id,
            @PathVariable Long productSkuId,
            @Valid @ModelAttribute ProductSkuRequest productSkuRequest
    ) throws IOException {
        productSkuService.updateProductSku(id, productSkuId, productSkuRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/productSkus/{productSkuId}/disable")
    public ResponseEntity<Void> disableProductSku(@PathVariable Long id, @PathVariable Long productSkuId) {
        productSkuService.disableProductSku(id, productSkuId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/productSkus/{productSkuId}/enable")
    public ResponseEntity<Void> enableProductSku(@PathVariable Long id, @PathVariable Long productSkuId) {
        productSkuService.enableProductSku(id, productSkuId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
