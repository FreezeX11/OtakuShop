package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemResponse {
    private Long id;

    private int quantity;

    private ProductResponse productResponse;

    private ProductSkuResponse productSkuResponse;
}
