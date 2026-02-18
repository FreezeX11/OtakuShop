package com.Backend.Mapper;

import com.Backend.Entity.Variation;
import com.Backend.Payload.Request.VariationRequest;
import com.Backend.Payload.Response.VariationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class VariationMapper {
    private final VariationValueMapper variationValueMapper;

    public Variation toVariation(VariationRequest variationRequest) {
        Variation variation = new Variation();

        variation.setName(variationRequest.getName());
        variation.setCreatedDate(LocalDateTime.now());

        return variation;
    }

    public VariationResponse toVariationResponse(Variation variation) {
        VariationResponse variationResponse = new VariationResponse();

        variationResponse.setId(variation.getId());
        variationResponse.setName(variation.getName());
        variationResponse.setEnable(variation.isEnable());

        variationResponse.setVariationValueResponses(variation.getVariationValues().stream()
                .map(variationValueMapper::toVariationValueResponse)
                .toList());

        return variationResponse;
    }
}
