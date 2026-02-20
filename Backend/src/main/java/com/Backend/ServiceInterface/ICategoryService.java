package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.CategoryRequest;
import com.Backend.Payload.Response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    void addCategory(CategoryRequest categoryRequest);
    void updateCategory(Long id, CategoryRequest categoryRequest);
    void enableCategory(Long id);
    void disableCategory(Long id);
    void deleteCategory(Long id);
    CategoryResponse getCategory(Long id);
    List<CategoryResponse> getCategories();
}
