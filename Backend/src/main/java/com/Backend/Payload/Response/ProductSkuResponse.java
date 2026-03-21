package com.Backend.Payload.Response;

import com.Backend.Entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductSkuResponse {
    private Long id;

    private String sku;

    private int quantity;

    private List<VariationValueResponse> variationValueResponses;

    private List<ImageResponse> imageResponses;

    private boolean outOfStock;

    private boolean enable;
}
