package com.devteria.hello_spring_boot.Service;

import com.devteria.hello_spring_boot.Entity.Category;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Respository.categoryRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class categoryService implements IcategoryService{
    private final categoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new ResoureeNotFoundException("Not found Id")) ;
    }

    @Override
    public List<Category> getCategoryAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByName(String nameCategory) {
        return categoryRepository.findCategoryByNameCategory(nameCategory);
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(category1 -> !categoryRepository.existsByNameCategory(category1.getNameCategory()))
                .map(categoryRepository::save)
                .orElseThrow(()->new ResoureeNotFoundException(category.getNameCategory()+" already exits"));
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory->{
            oldCategory.setNameCategory(category.getNameCategory());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(()->new ResoureeNotFoundException("category not found"));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,()->{
                    throw new ResoureeNotFoundException("not found id");
        });

    }
}
