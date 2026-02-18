package com.Backend.Controller;

import com.Backend.Payload.Request.VariationRequest;
import com.Backend.Payload.Request.VariationValueRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.VariationValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/variationValues")
public class VariationValueController {
    private final VariationValueService variationValueService;

    @PostMapping
    public ResponseEntity<Void> addVariationValue(@Valid @RequestBody VariationValueRequest variationValueRequest) {
        variationValueService.addVariationValue(variationValueRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVariationValue(
            @PathVariable Long id,
            @Valid @RequestBody VariationValueRequest variationValueRequest
    ) {
        variationValueService.updateVariationValue(id, variationValueRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableVariationValue(@PathVariable Long id) {
        variationValueService.disableVariationValue(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableVariationValue(@PathVariable Long id) {
        variationValueService.enableVariationValue(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getVariationValue(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                variationValueService.getVariationValue(id)
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getVariationValues() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                variationValueService.getVariationValues()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }
}
