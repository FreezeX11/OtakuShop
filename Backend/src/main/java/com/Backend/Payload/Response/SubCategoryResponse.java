package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubCategoryResponse {
    private Long id;

    private String name;

    private String categoryName;

    private boolean enable;
}
