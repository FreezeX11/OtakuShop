package com.Backend.Service;

import com.Backend.Entity.*;
import com.Backend.Exception.BusinessException;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.CartItemMapper;
import com.Backend.Mapper.CartMapper;
import com.Backend.Payload.Request.CartItemRequest;
import com.Backend.Payload.Response.CartResponse;
import com.Backend.Repository.CartItemRepository;
import com.Backend.Repository.CartRepository;
import com.Backend.Repository.ProductRepository;
import com.Backend.Repository.ProductSkuRepository;
import com.Backend.ServiceInterface.ICartService;
import com.Backend.Util.AuthUtil;
import com.Backend.Util.CartUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartUtil cartUtil;
    private final AuthUtil authUtil;
    private final CartItemMapper cartItemMapper;
    private final CartMapper cartMapper;

    @Override
    public void createCart(User user) {
        Cart cart = new Cart();

        cart.setUser(user);
        cart.setCreatedDate(LocalDateTime.now());

        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void addProductInCart(CartItemRequest cartItemRequest) {
        ProductSku existingProductSku = productSkuRepository.findById(cartItemRequest.getProductSkuId())
                .orElseThrow(() -> new ResourceNotFoundException("This product variant doesn't exist"));

        Cart cart = authUtil.loggedInUser().getCart();

        BigDecimal price = existingProductSku.getProduct().getPrice();

        if(cartItemRepository.existsByProductSkuIdAndCartId(
                cartItemRequest.getProductSkuId(),
                cart.getId())
        ) {
            throw new ResourceAlreadyExistException("This product is already in cart");
        }

        if(existingProductSku.getQuantity() == 0) {
            throw new BusinessException("Product " + existingProductSku.getProduct().getName() + " is not available");
        }

        if(existingProductSku.getQuantity() < cartItemRequest.getQuantity()) {
            throw new BusinessException("Please, make an order"
                    + " less than or equal to the quantity " + existingProductSku.getQuantity());
        }

        CartItem cartItem = cartItemMapper.toCartItem(cartItemRequest);

        cartItem.setPrice(price);
        cartItem.setCart(cart);
        cartItem.setProductSku(existingProductSku);

        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(
                price.multiply(BigDecimal.valueOf(cartItemRequest.getQuantity()))
                        .add(cart.getTotalPrice())
        );
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateProductQuantityInCart(Long productSkuId, int quantity) {
        ProductSku existingProductSku = productSkuRepository.findById(productSkuId)
                .orElseThrow(() -> new ResourceNotFoundException("This product variant doesn't exist"));

        Cart cart = authUtil.loggedInUser().getCart();
        BigDecimal price = existingProductSku.getProduct().getPrice();

        CartItem existingCartItem = cartItemRepository.findByProductSkuIdAndCartId(productSkuId, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("This product is not in cart"));

        int newQuantity = existingCartItem.getQuantity() + quantity;

        if(newQuantity < 0) {
            throw new IllegalArgumentException("Quantity should be greater than 0");
        }

        if(existingProductSku.getQuantity() == 0) {
            throw new BusinessException("Product " + existingProductSku.getProduct().getName() + " is not available");
        }

        if(existingProductSku.getQuantity() < quantity) {
            throw new BusinessException("Please, make an order"
                    + " less than or equal to the quantity " + existingProductSku.getQuantity());
        }

        if(newQuantity == 0) {
            deleteProductInCart(productSkuId);
        } else {
            existingCartItem.setQuantity(newQuantity);
            cart.setTotalPrice(
                    price.multiply(BigDecimal.valueOf(quantity))
                            .add(cart.getTotalPrice())
            );
            cartRepository.save(cart);
        }
    }

    @Override
    public void deleteProductInCart(Long productSkuId) {
        Cart cart = authUtil.loggedInUser().getCart();

        CartItem existingCartItem = cartItemRepository.findByProductSkuIdAndCartId(productSkuId, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("This product is not in cart"));

        cart.getCartItems().remove(existingCartItem);
        cart.setTotalPrice(
                cart.getTotalPrice().subtract(
                        existingCartItem.getPrice()
                                .multiply(BigDecimal.valueOf(existingCartItem.getQuantity()))
                )
        );

        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateProductInCart(Long productId) { //to many save need to learn more about Hibernate
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("This product doesn't exist"));

        List<Long> productSkusIds = existingProduct.getProductSkus().stream()
                .map(ProductSku::getId)
                .toList();

        List<CartItem> cartItems = cartItemRepository.findByProductSkuIdIn(productSkusIds);

        Set<Cart> cartsToUpdate = new HashSet<>();

        cartItems.forEach(cartItem -> {
            Cart updatedCart = cartUtil.recalculateCartTotalForUpdatedItem(cartItem, existingProduct);
            cartsToUpdate.add(updatedCart);
        });

        cartRepository.saveAll(cartsToUpdate);
    }

    @Override
    public CartResponse getCart() {
        Cart cart = authUtil.loggedInUser().getCart();

        return cartMapper.toCartResponse(cart);
    }
}
