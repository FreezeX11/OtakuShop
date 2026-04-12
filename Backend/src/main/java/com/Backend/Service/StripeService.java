package com.Backend.Service;

import com.Backend.Entity.Order;
import com.Backend.Payload.Response.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {
    @Value("${stripe.secretKey}")
    private String secretKey;

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


}
