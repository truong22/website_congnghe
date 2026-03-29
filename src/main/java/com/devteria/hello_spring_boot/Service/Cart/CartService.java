package com.devteria.hello_spring_boot.Service.Cart;

import com.devteria.hello_spring_boot.Entity.Cart;
import com.devteria.hello_spring_boot.Entity.User;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Respository.CartItemRepository;
import com.devteria.hello_spring_boot.Respository.CartRepository;
import com.devteria.hello_spring_boot.dto.CartDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    @Transactional
    @Override
    public Cart getCart(Long id) {
      return  cartRepository.findById(id).orElseThrow(()->new ResoureeNotFoundException("not found id"));

    }
    @Transactional // QUAN TRỌNG: Giữ Session mở để ModelMapper quét qua CartItems
    @Override
    public CartDto getCartDto(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResoureeNotFoundException("Cart not found"));

        // Ép Hibernate tải danh sách items trước khi map
        if (cart.getCartItems() != null) {
            cart.getCartItems().size();
        }

        return modelMapper.map(cart, CartDto.class);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart =getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        if (cart.getCartItems() != null) {
            cart.getCartItems().clear();
        }

        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart=getCart(id);

        return cart.getTotalAmount();
    }
    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
        .orElseGet(()->{
            Cart newcart=new Cart();
            newcart.setUser(user);
            return cartRepository.save(newcart);
        });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);    }
}
