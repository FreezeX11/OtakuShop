package com.Backend.Controller;

import com.Backend.Payload.Request.AddressRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.AddressService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@AllArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<Void> addAddress(@Valid @RequestBody AddressRequest addressRequest) {
        addressService.addAddress(addressRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest addressRequest
    ) {
        addressService.updateAddress(id, addressRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAddressesByUser() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                addressService.getAddressesByUser()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }
}
