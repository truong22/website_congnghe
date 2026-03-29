package com.devteria.hello_spring_boot.Respository;

import com.devteria.hello_spring_boot.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    void deleteAllByCartId(Long id);
}
