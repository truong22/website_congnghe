package com.devteria.hello_spring_boot.Respository;

import com.devteria.hello_spring_boot.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface orderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long id);
}
