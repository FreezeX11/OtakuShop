package com.Backend.Service;

import com.Backend.Entity.*;
import com.Backend.Enumeration.OrderStatus;
import com.Backend.Enumeration.PaymentMethod;
import com.Backend.Enumeration.PaymentStatus;
import com.Backend.Exception.BusinessException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.OrderMapper;
import com.Backend.Payload.Response.OrderResponse;
import com.Backend.Payload.Response.StripeResponse;
import com.Backend.Repository.AddressRepository;
import com.Backend.Repository.OrderRepository;
import com.Backend.ServiceInterface.IOrderService;
import com.Backend.Util.AuthUtil;
import com.Backend.Util.OrderUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderUtil orderUtil;
    private final AuthUtil authUtil;
    private final StripeService stripeService;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public OrderResponse placeOrder(Long addressId, String paymentMethod) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("This Shipping Address doesn't exist"));

        User user = authUtil.loggedInUser();
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItem> cartItemsCopy = new ArrayList<>(cartItems);

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
        order.setOrderStatus(OrderStatus.PENDING);

        //created new payment
        payment.setAmount(order.getTotalPrice());
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setOrder(order);
        payment.setCreatedDate(LocalDateTime.now());
        order.setPayment(payment);

        cartItemsCopy.forEach(cartItem -> {
                    OrderItem orderItem = new OrderItem();

                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setProductSku(cartItem.getProductSku());
                    orderItem.setOrder(order);
                    orderItem.setCreatedDate(LocalDateTime.now());
                    order.getOrderItems().add(orderItem);
                });

        if(paymentMethod.equalsIgnoreCase(PaymentMethod.CASH_ON_DELIVRY.toString())) {
            orderUtil.updateStockAndCart(cartItemsCopy);
        }

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public StripeResponse checkout(Long id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This order doesn't exist"));

        User user = authUtil.loggedInUser();

        if (!(Objects.equals(user.getId(), existingOrder.getAddress().getUser().getId()))) {
            throw new BusinessException("This order doesn't belong to this user");
        }

        if (!existingOrder.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new BusinessException("Order's already processed or invalid");
        }

        return stripeService.createCheckoutSession(existingOrder);
    }

    @Override
    public List<OrderResponse> getOrders() {
        User user = authUtil.loggedInUser();

        return orderRepository.findByEmail(user.getEmail()).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public void updateOrderStatus(Long id, String status) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This order doesn't exist"));

        existingOrder.setOrderStatus(OrderStatus.valueOf(status));
        orderRepository.save(existingOrder);
    }
}
