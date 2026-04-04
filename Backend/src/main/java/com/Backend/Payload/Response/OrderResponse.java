package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private Long id;

    private String email;

    private String orderStatus;

    private BigDecimal totalPrice;

    private List<OrderItemResponse> orderItemResponses;

    private AddressResponse addressResponse;
}
