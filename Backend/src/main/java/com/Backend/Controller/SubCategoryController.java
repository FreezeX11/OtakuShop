package com.Backend.Controller;

import com.Backend.Payload.Request.SubCategoryRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.SubCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subcategories")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @PostMapping
    public ResponseEntity<Void> addSubCategory(@Valid @RequestBody SubCategoryRequest subCategoryRequest) {
        subCategoryService.addSubCategory(subCategoryRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSubCategory(
            @PathVariable Long id,
            @Valid @RequestBody SubCategoryRequest subCategoryRequest
    ) {
        subCategoryService.updateSubCategory(id, subCategoryRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableSubCategory(@PathVariable Long id) {
        subCategoryService.disableSubCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableSubCategory(@PathVariable Long id) {
        subCategoryService.enableSubCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSubCategory(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                subCategoryService.getSubCategory(id)
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getSubCategories() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                subCategoryService.getSubCategories()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }
}
