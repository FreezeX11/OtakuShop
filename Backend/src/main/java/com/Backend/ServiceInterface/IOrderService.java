package com.Backend.ServiceInterface;

import com.Backend.Entity.Order;
import com.Backend.Payload.Response.OrderResponse;
import com.Backend.Payload.Response.StripeResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse placeOrder(Long addressId, String paymentMethod);
    StripeResponse checkout(Long id);
    List<OrderResponse> getOrders();
    void updateOrderStatus(Long id, String status);
}
