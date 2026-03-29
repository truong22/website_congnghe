package com.devteria.hello_spring_boot.Service.CartItem;

import com.devteria.hello_spring_boot.Entity.CartItem;

public interface ICartItemService {
    void  addItemToCart(Long cartId,Long productId,int quantity);
    void removeItemFromCart(Long cartId,Long productId);
    void updateItemQuantity(Long cartId,Long productId,int quantity);
    CartItem getCartItem(Long cartId,Long productId);
}
