package com.Backend.Payload.Request;

import com.Backend.Entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductRequest {
    @NotBlank(message = "Product name can't be null")
    @Size(min = 2, max = 15, message = "Product name must be between 2 and 15 characters")
    private String name;

    @NotBlank(message = "Product description can't be null")
    @Size(min = 15, message = "Product description must at least 15 characters")
    private String description;

    @NotNull(message = "Product image is required")
    private MultipartFile cover;

    @NotNull(message = "The price can't be null")
    private BigDecimal price;

    @NotNull(message = "SubCategory Id can't be null")
    private Long subCategoryId;

    @NotEmpty(message = "ProductSku list can't be empty")
    @Size(min = 1, message = "ProductSkuRequest should have at least 1 element")
    private List<ProductSkuRequest> productSkuRequests = new ArrayList<>();

    @NotNull(message = "Tag list can't be null")
    private List<Long> tagsIds = new ArrayList<>();
}
