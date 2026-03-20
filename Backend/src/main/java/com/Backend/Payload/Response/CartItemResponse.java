package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartItemResponse {
    private Long id;

    private String name;

    private int quantity;

    private BigDecimal price;

    private List<VariationValueResponse> variationValueResponses;
}
