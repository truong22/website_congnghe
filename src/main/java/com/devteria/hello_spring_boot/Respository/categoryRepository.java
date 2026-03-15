package com.devteria.hello_spring_boot.Respository;

import com.devteria.hello_spring_boot.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface categoryRepository extends JpaRepository<Category,Long> {
    Category findCategoryByNameCategory(String nameCategory);

    boolean existsByNameCategory(String nameCategory);
}
