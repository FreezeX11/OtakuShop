package com.Backend.Mapper;

import com.Backend.Entity.VariationValue;
import com.Backend.Payload.Request.VariationValueRequest;
import com.Backend.Payload.Response.VariationValueResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
public class VariationValueMapper {

    public VariationValue toVariationValue(VariationValueRequest variationValueRequest) {
        VariationValue variationValue = new VariationValue();
        variationValue.setName(variationValueRequest.getName());
        variationValue.setCreatedDate(LocalDateTime.now());

        return variationValue;
    }

    public VariationValueResponse toVariationValueResponse(VariationValue variationValue) {
        VariationValueResponse variationValueResponse = new VariationValueResponse();

        variationValueResponse.setId(variationValue.getId());
        variationValueResponse.setVariationName(variationValue.getVariation().getName());
        variationValueResponse.setName(variationValue.getName());
        variationValueResponse.setEnable(variationValue.isEnable());

        return variationValueResponse;
    }
}
