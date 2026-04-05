package com.Backend.Controller;

import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> placeOrder(
            @RequestParam Long addressId,
            @RequestParam String paymentMethod
    ) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.CREATED.value(),
                orderService.placeOrder(addressId, paymentMethod)
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        orderService.updateOrderStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getOrders() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                orderService.getOrders()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

}
