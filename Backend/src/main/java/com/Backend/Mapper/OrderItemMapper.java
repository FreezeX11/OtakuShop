package com.Backend.Mapper;

import com.Backend.Entity.CartItem;
import com.Backend.Entity.OrderItem;
import com.Backend.Payload.Response.CartItemResponse;
import com.Backend.Payload.Response.OrderItemResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderItemMapper {
    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse orderItemResponse = new OrderItemResponse();

        orderItemResponse.setId(orderItem.getId());
        orderItemResponse.setQuantity(orderItem.getQuantity());
        orderItemResponse.setProductResponse(productMapper.toProductResponse(orderItem.getProductSku().getProduct()));
        orderItemResponse.setProductSkuResponse(productSkuMapper.toProductSkuResponse(orderItem.getProductSku()));

        return orderItemResponse;
    }

}
