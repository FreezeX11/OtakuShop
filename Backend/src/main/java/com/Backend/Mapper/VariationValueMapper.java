package com.Backend.Mapper;

import com.Backend.Entity.VariationValue;
import com.Backend.Payload.Request.VariationValueRequest;
import com.Backend.Payload.Response.VariationValueResponse;
import org.springframework.stereotype.Service;

@Service
public class VariationValueMapper {

    public VariationValue toVariationValue(VariationValueRequest variationValueRequest) {
        VariationValue variationValue = new VariationValue();
        variationValue.setName(variationValueRequest.getName());

        return variationValue;
    }

    public VariationValueResponse toVariationValueResponse(VariationValue variationValue) {
        VariationValueResponse variationValueResponse = new VariationValueResponse();

        variationValueResponse.setId(variationValue.getId());
        variationValueResponse.setName(variationValue.getName());
        variationValueResponse.setEnable(variationValue.isEnable());

        return variationValueResponse;
    }
}
