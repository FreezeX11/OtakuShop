package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.VariationValueRequest;
import com.Backend.Payload.Response.VariationValueResponse;

import java.util.List;

public interface IVariationValueService {
    void addVariationValue(VariationValueRequest variationValueRequest);
    void updateVariationValue(Long id, VariationValueRequest variationValueRequest);
    void enableVariationValue(Long id);
    void disableVariationValue(Long id);
    void deleteVariationValue(Long id);
    VariationValueResponse getVariationValue(Long id);
    List<VariationValueResponse> getVariationValues();
}
