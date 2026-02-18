package com.Backend.Service;

import com.Backend.Entity.Category;
import com.Backend.Entity.SubCategory;
import com.Backend.Exception.BusinessException;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.SubCategoryMapper;
import com.Backend.Payload.Request.SubCategoryRequest;
import com.Backend.Payload.Response.SubCategoryResponse;
import com.Backend.Repository.CategoryRepository;
import com.Backend.Repository.SubCategoryRepository;
import com.Backend.ServiceInterface.ISubCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubCategoryService implements ISubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper subCategoryMapper;

    @Override
    public void addSubCategory(SubCategoryRequest subCategoryRequest) {
        if (subCategoryRepository.findByNameIgnoreCase(subCategoryRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This subcategory already exist");

        Category existingCategory = categoryRepository.findById(subCategoryRequest.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("This category doesn't exist"));

        SubCategory subCategory = subCategoryMapper.toSubCategory(subCategoryRequest);
        subCategory.setCategory(existingCategory);

        subCategoryRepository.save(subCategory);
    }

    @Override
    public void updateSubCategory(Long id, SubCategoryRequest subCategoryRequest) {
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This subcategory doesn't exist"));

        existingSubCategory.setName(subCategoryRequest.getName());
        subCategoryRepository.save(existingSubCategory);
    }

    @Override
    public void enableSubCategory(Long id) {
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This subcategory doesn't exist"));

        Category existingCategory = existingSubCategory.getCategory();

        if(!existingCategory.isEnable()) {
            throw new BusinessException("Category is disabled, cannot update subcategories");
        }

        existingSubCategory.setEnable(true);
        subCategoryRepository.save(existingSubCategory);
    }

    @Override
    public void disableSubCategory(Long id) {
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This subcategory doesn't exist"));

        Category existingCategory = existingSubCategory.getCategory();

        if(!existingCategory.isEnable()) {
            throw new BusinessException("Category is disabled, cannot update subcategories");
        }

        existingSubCategory.setEnable(false);
        subCategoryRepository.save(existingSubCategory);
    }

    @Override
    public SubCategoryResponse getSubCategory(Long id) {
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This subcategory doesn't exist"));

        return subCategoryMapper.toSubCategoryResponse(existingSubCategory);
    }

    @Override
    public List<SubCategoryResponse> getSubCategories() {
        return subCategoryRepository.findAll().stream()
                .map(subCategoryMapper::toSubCategoryResponse)
                .toList();
    }
}
