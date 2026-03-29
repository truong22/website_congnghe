package com.devteria.hello_spring_boot.Service.Cart;

import com.devteria.hello_spring_boot.Entity.Cart;
import com.devteria.hello_spring_boot.Entity.User;
import com.devteria.hello_spring_boot.dto.CartDto;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);
    CartDto getCartDto(Long cartId);}
