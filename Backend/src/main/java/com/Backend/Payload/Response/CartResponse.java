package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartResponse {
    private Long id;

    private List<CartItemResponse> cartItemResponses;

    private BigDecimal totalPrice;
}
