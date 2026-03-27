package com.Backend.Mapper;

import com.Backend.Entity.Category;
import com.Backend.Payload.Request.CategoryRequest;
import com.Backend.Payload.Response.CategoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CategoryMapper {
    private final SubCategoryMapper subCategoryMapper;

    public Category toCategory(CategoryRequest categoryRequest) {
        Category category = new Category();

        category.setName(categoryRequest.getName());
        category.setCreatedDate(LocalDateTime.now());

        return category;
    }

    public CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setEnable(category.isEnable());
        categoryResponse.setSubCategoryResponses(category.getSubCategories().stream()
                .map(subCategoryMapper::toSubCategoryResponse)
                .toList());

        return categoryResponse;
    }
}
