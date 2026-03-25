package com.devteria.hello_spring_boot.Respository;

import com.devteria.hello_spring_boot.Entity.image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface imageRepository extends JpaRepository<image,Long> {
    List<image> findByProductsId(Long id);
}
