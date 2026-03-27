package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VariationValueResponse {
    private Long id;

    private String variationName;

    private String name;

    private boolean enable;
}
