package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.VariationRequest;
import com.Backend.Payload.Response.VariationResponse;

import java.util.List;

public interface IVariationService {
    void addVariation(VariationRequest variationRequest);
    void updateVariation(Long id, VariationRequest variationRequest);
    void enableVariation(Long id);
    void disableVariation(Long id);
    void deleteVariation(Long id);
    VariationResponse getVariation(Long id);
    List<VariationResponse> getVariations();
}
