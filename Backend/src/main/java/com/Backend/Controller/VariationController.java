package com.Backend.Controller;

import com.Backend.Payload.Request.VariationRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.VariationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/variations")
public class VariationController {
    private final VariationService variationService;

    @PostMapping
    public ResponseEntity<Void> addVariation(@Valid @RequestBody VariationRequest variationRequest) {
        variationService.addVariation(variationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVariation(
            @PathVariable Long id,
            @Valid @RequestBody VariationRequest variationRequest
    ) {
        variationService.updateVariation(id, variationRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableVariation(@PathVariable Long id) {
        variationService.disableVariation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableVariation(@PathVariable Long id) {
        variationService.enableVariation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getVariation(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                variationService.getVariation(id)
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getVariations() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                variationService.getVariations()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }
}
