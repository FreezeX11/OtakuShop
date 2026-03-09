package com.Backend.Payload.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductSkuRequest {
    @NotNull(message = "quantity can't be null")
    private int quantity;

    @NotNull(message = "Variation list can't be null")
    private List<Long> variationValuesIds = new ArrayList<>();

    @NotNull(message = "File List can't be null")
    private List<MultipartFile> files = new ArrayList<>();
}
