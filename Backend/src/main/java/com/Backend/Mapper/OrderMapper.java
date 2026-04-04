package com.Backend.Mapper;

import com.Backend.Entity.Order;
import com.Backend.Payload.Response.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    public OrderResponse toOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order.getId());
        orderResponse.setEmail(order.getEmail());
        orderResponse.setOrderStatus(String.valueOf(order.getOrderStatus()));
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setAddressResponse(addressMapper.toAddressResponse(order.getAddress()));
        orderResponse.setOrderItemResponses(
                order.getOrderItems().stream()
                        .map(orderItemMapper::toOrderItemResponse)
                        .toList()
        );

        return orderResponse;
    }

}
