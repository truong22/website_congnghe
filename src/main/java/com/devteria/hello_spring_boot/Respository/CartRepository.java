package com.devteria.hello_spring_boot.Respository;

import com.devteria.hello_spring_boot.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
   Cart findByUserId(Long userId);
}
