package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {
    private Long id;

    private String name;

    private String description;

    private String cover;

    private BigDecimal price;

    private SubCategoryResponse subCategoryResponse;

    private List<TagResponse> tagResponses;

    private List<ProductSkuResponse> productSkuResponses;
}
