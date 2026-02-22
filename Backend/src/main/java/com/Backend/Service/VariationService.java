package com.Backend.Service;

import com.Backend.Entity.ProductSku;
import com.Backend.Entity.Variation;
import com.Backend.Entity.VariationValue;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceInUseException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.VariationMapper;
import com.Backend.Payload.Request.VariationRequest;
import com.Backend.Payload.Response.VariationResponse;
import com.Backend.Repository.VariationRepository;
import com.Backend.ServiceInterface.IVariationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariationService implements IVariationService {
    private final VariationRepository variationRepository;
    private final VariationMapper variationMapper;

    @Override
    public void addVariation(VariationRequest variationRequest) {
        if (variationRepository.findByNameIgnoreCase(variationRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This variation already exist");

        variationRepository.save(variationMapper.toVariation(variationRequest));
    }

    @Override
    public void updateVariation(Long id, VariationRequest variationRequest) {
        Variation existingVariation = variationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This variation doesn't exist"));

        if (variationRepository.findByNameIgnoreCase(variationRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This variation already exist");

        existingVariation.setName(variationRequest.getName());
        variationRepository.save(existingVariation);
    }

    @Override
    public void enableVariation(Long id) {
        Variation existingVariation = variationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This variation doesn't exist"));

        List<VariationValue> variationValues = existingVariation.getVariationValues();

        for(VariationValue variationValue : variationValues) {
            variationValue.getProductSkus()
                    .forEach(productSku -> productSku.setEnable(true));

            variationValue.setEnable(true);
        }

        existingVariation.setEnable(true);
        variationRepository.save(existingVariation);
    }

    @Override
    public void disableVariation(Long id) {
        Variation existingVariation = variationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This variation doesn't exist"));

        List<VariationValue> variationValues = existingVariation.getVariationValues();

        for(VariationValue variationValue : variationValues) {
            variationValue.getProductSkus()
                            .forEach(productSku -> productSku.setEnable(false));

            variationValue.setEnable(false);
        }

        existingVariation.setEnable(false);
        variationRepository.save(existingVariation);
    }

    @Override
    public void deleteVariation(Long id) {
        Variation existingVariation = variationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This variation doesn't exist"));

        List<VariationValue> variationValues = existingVariation.getVariationValues();

        for(VariationValue variationValue : variationValues) {
            boolean usedByActiveSku = variationValue.getProductSkus().stream()
                    .anyMatch(ProductSku::isEnable);

            if(usedByActiveSku)
                throw new ResourceInUseException(
                        "The variation value : " + variationValue + " is used by one or many products"
                );
        }

        variationRepository.delete(existingVariation);
    }

    @Override
    public VariationResponse getVariation(Long id) {
        Variation existingVariation = variationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This variation doesn't exist"));

        return variationMapper.toVariationResponse(existingVariation);
    }

    @Override
    public List<VariationResponse> getVariations() {
        return variationRepository.findAll().stream()
                .map(variationMapper::toVariationResponse)
                .toList();
    }
}
