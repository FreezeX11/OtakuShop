package com.Backend.Service;

import com.Backend.Entity.Cart;
import com.Backend.Entity.CartItem;
import com.Backend.Entity.ProductSku;
import com.Backend.Entity.User;
import com.Backend.Exception.BusinessException;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.CartItemMapper;
import com.Backend.Payload.Request.CartItemRequest;
import com.Backend.Repository.CartItemRepository;
import com.Backend.Repository.CartRepository;
import com.Backend.Repository.ProductSkuRepository;
import com.Backend.ServiceInterface.ICartService;
import com.Backend.Util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final ProductSkuRepository productSkuRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthUtil authUtil;
    private final CartItemMapper cartItemMapper;

    @Override
    public void createCart(User user) {
        Cart cart = new Cart();

        cart.setUser(user);
        cart.setCreatedDate(LocalDateTime.now());

        cartRepository.save(cart);
    }

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

        if(existingProductSku.getQuantity() < cartItemRequest.getQuantity()) {
            throw new BusinessException("Please, make an order"
                    + " less than or equal to the quantity " + existingProductSku.getQuantity() + ".");
        }

        CartItem cartItem = cartItemMapper.toCartItem(cartItemRequest);

        cartItem.setPrice(price);
        cartItem.setCart(cart);
        cartItem.setProductSku(existingProductSku);

        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(price.multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())).add(cart.getTotalPrice()));

        cartRepository.save(cart);
    }

    @Override
    public void updateProductQuantityInCart(int quantity) {
        

    }

    @Override
    public void deleteProductInCart() {

    }
}
