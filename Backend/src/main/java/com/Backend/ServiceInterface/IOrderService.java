package com.Backend.ServiceInterface;

import com.Backend.Entity.Order;
import com.Backend.Payload.Response.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse placeOrder(Long addressId, String paymentMethod);
    List<OrderResponse> getOrders();
}
