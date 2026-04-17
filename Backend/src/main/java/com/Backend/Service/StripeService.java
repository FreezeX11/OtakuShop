package com.Backend.Service;

import com.Backend.Entity.*;
import com.Backend.Enumeration.OrderStatus;
import com.Backend.Enumeration.PaymentMethod;
import com.Backend.Enumeration.PaymentStatus;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Payload.Response.StripeResponse;
import com.Backend.Repository.OrderRepository;
import com.Backend.Util.AuthUtil;
import com.Backend.Util.OrderUtil;
import com.Backend.Util.UserUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StripeService {
    @Value("${stripe.secretKey}")
    private String secretKey;

    @Value("${stripe.endpointSecret}")
    private String endpointSecret;

    private final OrderRepository orderRepository;

    private final OrderUtil orderUtil;
    
    private final AuthUtil authUtil;

    public StripeResponse createCheckoutSession(Order order) {
        Stripe.apiKey = secretKey;
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        order.getOrderItems().forEach(
                orderItem -> {
                    SessionCreateParams.LineItem.PriceData.ProductData productData =
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(orderItem.getProductSku().getProduct().getName())
                                    .build();

                    SessionCreateParams.LineItem.PriceData priceData =
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("USD")
                                    .setUnitAmount(orderItem.getProductSku().getProduct().getPrice().longValue() * 100)
                                    .setProductData(productData)
                                    .build();

                    SessionCreateParams.LineItem lineItem =
                            SessionCreateParams
                                    .LineItem.builder()
                                    .setQuantity((long) orderItem.getQuantity())
                                    .setPriceData(priceData)
                                    .build();

                    lineItems.add(lineItem);
                }
        );

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/success")
                        .setCancelUrl("http://localhost:8080/cancel")
                        .putMetadata("orderId", String.valueOf(order.getId()))
                        .addAllLineItem(lineItems)
                        .build();

        Session session = null;
        try {
             session = Session.create(params);
        } catch (StripeException e) {
            throw new com.Backend.Exception.StripeException("Something went wrong with session ", e);
        }

        return new StripeResponse(
                "Success",
                "Payment session created",
                session.getId(),
                session.getUrl()
        );
    }

    public void handleStripeEvent(HttpServletRequest request) throws Exception {
        String payload = request.getReader().lines().collect(Collectors.joining());
        String sigHeader = request.getHeader("Stripe-Signature");

        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElse(null);

        if (intent == null)
            return;

        String orderId = intent.getMetadata().get("orderId");

        Order order = orderRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new ResourceNotFoundException("This order doesn't exist"));

        Payment payment = order.getPayment();

        if (payment.getPaymentStatus() == PaymentStatus.PAID)
            return;

        if (!"payment_intent.succeeded".equals(event.getType()) &&
                !"payment_intent.payment_failed".equals(event.getType())) {
            return;
        }

        if ("payment_intent.succeeded".equals(event.getType())) {

            order.setOrderStatus(OrderStatus.CONFIRMED);

            payment.setPaymentStatus(PaymentStatus.PAID);

            orderUtil.updateStock(order.getOrderItems());

        } else if ("payment_intent.payment_failed".equals(event.getType())) {

            payment.setPaymentStatus(PaymentStatus.FAILED);

        }

        orderRepository.save(order);
    }

}
