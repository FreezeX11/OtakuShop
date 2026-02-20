package com.Backend.Mapper;

import com.Backend.Entity.SubCategory;
import com.Backend.Payload.Request.SubCategoryRequest;
import com.Backend.Payload.Response.SubCategoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SubCategoryMapper {

    public SubCategory toSubCategory(SubCategoryRequest subCategoryRequest) {
        SubCategory subCategory = new SubCategory();

        subCategory.setName(subCategoryRequest.getName());
        subCategory.setCreatedDate(LocalDateTime.now());

        return subCategory;
    }

    public SubCategoryResponse toSubCategoryResponse(SubCategory subCategory) {
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();

        subCategoryResponse.setId(subCategory.getId());
        subCategoryResponse.setName(subCategory.getName());
        subCategoryResponse.setEnable(subCategory.isEnable());
        subCategoryResponse.setCategoryName(subCategory.getCategory().getName());

        return subCategoryResponse;
    }

}
