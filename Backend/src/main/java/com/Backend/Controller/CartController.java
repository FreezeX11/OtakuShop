package com.Backend.Controller;

import com.Backend.Payload.Request.CartItemRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addProductInCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        cartService.addProductInCart(cartItemRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/productSkus/{productSkuId}/quantity/{operation}")
    public ResponseEntity<Void> updateProductQuantityInCart(
            @PathVariable Long productSkuId,
            @PathVariable String operation
    ) {
        cartService.updateProductQuantityInCart(
                productSkuId,
                operation.equalsIgnoreCase("remove") ? -1 : 1
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/productSkus/{productSkuId}")
    public ResponseEntity<Void> deleteProductInCart(@PathVariable Long productSkuId) {
        cartService.deleteProductInCart(productSkuId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/cart")
    public ResponseEntity<ApiResponse> getCart() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                cartService.getCart()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }
}
