package com.Backend.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank(message = "Category name can't be null")
    @Size(min = 2, max = 15, message = "Category name must be between 2 and 15 characters")
    private String name;
}
