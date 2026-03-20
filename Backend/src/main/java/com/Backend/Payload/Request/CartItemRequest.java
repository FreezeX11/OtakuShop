package com.Backend.Payload.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    @NotNull(message = "quantity can't be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "productSkuId can't be null")
    private Long productSkuId;
}
