package com.Backend.Payload.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductSkuRequest {
    @NotNull(message = "quantity can't be null")
    private int quantity;

    private List<Long> variationValuesIds;

    private List<MultipartFile> files;
}
