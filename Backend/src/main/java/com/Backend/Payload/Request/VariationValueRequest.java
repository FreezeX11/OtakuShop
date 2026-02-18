package com.Backend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariationValueRequest {
    @NotBlank(message = "Variation name can't be null")
    @Size(min = 2, max = 15, message = "Variation name must be between 2 and 15 characters")
    private String name;

    @NotNull
    private Long variationId;
}
