package com.Backend.Controller;

import com.Backend.Payload.Request.CategoryRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        categoryService.updateCategory(id, categoryRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableCategory(@PathVariable Long id) {
        categoryService.disableCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableCategory(@PathVariable Long id) {
        categoryService.enableCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategory(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                categoryService.getCategory(id)
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getCategories() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                categoryService.getCategories()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }
}
