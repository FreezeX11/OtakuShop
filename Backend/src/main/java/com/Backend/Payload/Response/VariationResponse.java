package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VariationResponse {
    private Long id;

    private String name;

    private List<VariationValueResponse> variationValueResponses;

    private boolean enable;
}
