package com.project.shopapp.Service.Interface;

import com.project.shopapp.DTO.request.CategoryDTO;
import com.project.shopapp.Model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id,CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
