package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.SubCategoryRequest;
import com.Backend.Payload.Response.SubCategoryResponse;

import java.util.List;

public interface ISubCategoryService {
    void addSubCategory(SubCategoryRequest subCategoryRequest);
    void updateSubCategory(Long id, SubCategoryRequest subCategoryRequest);
    void enableSubCategory(Long id);
    void disableSubCategory(Long id);
    void deleteSubCategory(Long id);
    SubCategoryResponse getSubCategory(Long id);
    List<SubCategoryResponse> getSubCategories();
}
