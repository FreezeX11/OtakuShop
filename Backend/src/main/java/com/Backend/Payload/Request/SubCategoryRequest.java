package com.Backend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryRequest {
    @NotBlank(message = "SubCategory name can't be null")
    @Size(min = 2, max = 15, message = "SubCategory name must be between 2 and 15 characters")
    private String name;

    @NotNull
    private Long categoryId;
}
