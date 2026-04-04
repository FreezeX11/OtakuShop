package com.Backend.Service;

import com.Backend.Entity.*;
import com.Backend.Enumeration.OrderStatus;
import com.Backend.Enumeration.PaymentMethod;
import com.Backend.Enumeration.PaymentStatus;
import com.Backend.Exception.BusinessException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.OrderMapper;
import com.Backend.Payload.Response.OrderResponse;
import com.Backend.Repository.AddressRepository;
import com.Backend.Repository.OrderRepository;
import com.Backend.Repository.ProductSkuRepository;
import com.Backend.ServiceInterface.IOrderService;
import com.Backend.Util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ProductSkuRepository productSkuRepository;
    private final CartService cartService;
    private final AuthUtil authUtil;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public OrderResponse placeOrder(Long addressId, String paymentMethod) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("This Shipping Address doesn't exist"));

        User user = authUtil.loggedInUser();
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getCartItems();

        if(cartItems.isEmpty()) {
            throw new BusinessException("Cart is empty");
        }

        Order order = new Order();
        Payment payment = new Payment();

        //created new order
        order.setEmail(user.getEmail());
        order.setAddress(existingAddress);
        order.setTotalPrice(cart.getTotalPrice());
        order.setCreatedDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.CREATED);

        //created new payment
        payment.setAmount(order.getTotalPrice());
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setOrder(order);
        payment.setCreatedDate(LocalDateTime.now());
        order.getPayments().add(payment);

        cartItems.forEach(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    ProductSku existingProductSku = cartItem.getProductSku();

                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setProductSku(cartItem.getProductSku());
                    orderItem.setOrder(order);
                    orderItem.setCreatedDate(LocalDateTime.now());
                    order.getOrderItems().add(orderItem);

                    existingProductSku.setQuantity(existingProductSku.getQuantity() - cartItem.getQuantity());
                    cartService.deleteProductInCart(existingProductSku.getId());
                    productSkuRepository.save(existingProductSku);
                });
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrders() {
        User user = authUtil.loggedInUser();

        return orderRepository.findByEmail(user.getEmail()).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }
}
