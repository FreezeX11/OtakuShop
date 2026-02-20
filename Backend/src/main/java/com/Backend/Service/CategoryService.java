package com.Backend.Service;

import com.Backend.Entity.Category;
import com.Backend.Entity.SubCategory;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceInUseException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.CategoryMapper;
import com.Backend.Payload.Request.CategoryRequest;
import com.Backend.Payload.Response.CategoryResponse;
import com.Backend.Repository.CategoryRepository;
import com.Backend.ServiceInterface.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public void addCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.findByNameIgnoreCase(categoryRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This category already exist");

        categoryRepository.save(categoryMapper.toCategory(categoryRequest));
    }

    @Override
    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This category doesn't exist"));

        if (categoryRepository.findByNameIgnoreCase(categoryRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This category already exist");

        existingCategory.setName(categoryRequest.getName());
        categoryRepository.save(existingCategory);
    }

    @Override
    public void enableCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This category doesn't exist"));

        existingCategory.getSubCategories()
                .forEach(subCategory -> subCategory.setEnable(true));

        existingCategory.setEnable(true);
        categoryRepository.save(existingCategory);
    }

    @Override
    public void disableCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This category doesn't exist"));

        existingCategory.getSubCategories()
                .forEach(subCategory -> subCategory.setEnable(false));

        existingCategory.setEnable(false);
        categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This category doesn't exist"));

        List<SubCategory> subCategories = existingCategory.getSubCategories();

        for(SubCategory subCategory: subCategories) {
            if(!subCategory.getProducts().isEmpty())
                throw new ResourceInUseException("The subcategory: " + subCategory.getName() + "is used by one or more products");
        }

        categoryRepository.delete(existingCategory);
    }

    @Override
    public CategoryResponse getCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This category doesn't exist"));

        return categoryMapper.toCategoryResponse(existingCategory);
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }
}
