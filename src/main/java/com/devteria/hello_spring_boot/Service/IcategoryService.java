package com.devteria.hello_spring_boot.Service;

import com.devteria.hello_spring_boot.Entity.Category;

import java.util.List;

public interface IcategoryService {
    Category getCategoryById(Long id);
    List<Category>getCategoryAll();
    Category getCategoryByName(String nameCategory);
    Category addCategory(Category category);
    Category updateCategory(Long id,Category category);
    void deleteCategory(Long id);
}
