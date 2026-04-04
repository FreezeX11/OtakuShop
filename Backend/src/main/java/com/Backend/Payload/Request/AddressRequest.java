package com.Backend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    @NotBlank(message = "District shouldn't be null")
    private String district;

    @NotBlank(message = "Landmark shouldn't be null")
    private String landmark;
}
