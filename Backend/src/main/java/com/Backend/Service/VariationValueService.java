package com.Backend.Service;

import com.Backend.Entity.Variation;
import com.Backend.Entity.VariationValue;
import com.Backend.Exception.BusinessException;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceInUseException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.VariationValueMapper;
import com.Backend.Payload.Request.VariationValueRequest;
import com.Backend.Payload.Response.VariationValueResponse;
import com.Backend.Repository.VariationRepository;
import com.Backend.Repository.VariationValueRepository;
import com.Backend.ServiceInterface.IVariationValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariationValueService implements IVariationValueService {
    private final VariationValueRepository variationValueRepository;
    private final VariationRepository variationRepository;
    private final VariationValueMapper variationValueMapper;

    @Override
    public void addVariationValue(VariationValueRequest variationValueRequest) {
        if (variationValueRepository.findByNameIgnoreCase(variationValueRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This variation value already exist");

        Variation variation = variationRepository.findById(variationValueRequest.getVariationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variation with id: " + variationValueRequest.getVariationId() + "doesn't exist")
                );

        VariationValue variationValue = variationValueMapper.toVariationValue(variationValueRequest);
        variationValue.setVariation(variation);

        variationValueRepository.save(variationValue);
    }

    @Override
    public void updateVariationValue(Long id, VariationValueRequest variationValueRequest) {
        VariationValue existingVariationValue = variationValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "This variation value doesn't exist")
                );

        if (variationValueRepository.findByNameIgnoreCase(variationValueRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This variation value already exist");

        existingVariationValue.setName(variationValueRequest.getName());
        variationValueRepository.save(existingVariationValue);
    }

    @Override
    public void enableVariationValue(Long id) {
        VariationValue existingVariationValue = variationValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "This variation value doesn't exist")
                );

        Variation variation = existingVariationValue.getVariation();

        if(!variation.isEnable()) {
            throw new BusinessException("Variation is disabled, cannot update variation value");
        }

        existingVariationValue.getProductSkus()
                        .forEach(productSku -> productSku.setEnable(true));

        existingVariationValue.setEnable(true);
        variationValueRepository.save(existingVariationValue);
    }

    @Override
    public void disableVariationValue(Long id) {
        VariationValue existingVariationValue = variationValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "This variation value doesn't exist")
                );

        Variation variation = existingVariationValue.getVariation();

        if(!variation.isEnable()) {
            throw new BusinessException("Variation is disabled, cannot update variation value");
        }

        existingVariationValue.getProductSkus()
                .forEach(productSku -> productSku.setEnable(false));

        existingVariationValue.setEnable(false);
        variationValueRepository.save(existingVariationValue);
    }

    @Override
    public void deleteVariationValue(Long id) {
        VariationValue existingVariationValue = variationValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "This variation value doesn't exist")
                );

        if(!existingVariationValue.getProductSkus().isEmpty())
            throw new ResourceInUseException("This variation value is used by one or more products");

        variationValueRepository.delete(existingVariationValue);
    }

    @Override
    public VariationValueResponse getVariationValue(Long id) {
        VariationValue existingVariationValue = variationValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This variation value doesn't exist"));

        return variationValueMapper.toVariationValueResponse(existingVariationValue);
    }

    @Override
    public List<VariationValueResponse> getVariationValues() {
        return variationValueRepository.findAll().stream()
                .map(variationValueMapper::toVariationValueResponse)
                .toList();
    }
}
