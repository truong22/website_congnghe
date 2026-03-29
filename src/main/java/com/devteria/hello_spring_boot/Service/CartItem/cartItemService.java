package com.devteria.hello_spring_boot.Service.CartItem;

import com.devteria.hello_spring_boot.Entity.Cart;
import com.devteria.hello_spring_boot.Entity.CartItem;
import com.devteria.hello_spring_boot.Entity.Products;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Respository.CartItemRepository;
import com.devteria.hello_spring_boot.Respository.CartRepository;
import com.devteria.hello_spring_boot.Service.Cart.ICartService;
import com.devteria.hello_spring_boot.Service.Products.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class cartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;
    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart=cartService.getCart(cartId);
        Products products =productService.getProductById(productId);

        if(products.getInventory()<quantity){
            throw  new RuntimeException("San pham"+products.getNameProduct()+"khong du. Hien con :"+products.getInventory());
        }
        CartItem cartItem1=cart.getCartItems().stream()
                .filter(item->item.getProducts().getId().equals(productId))
                .findFirst().orElse(null);
        if(cartItem1==null){
            cartItem1=new CartItem();
            cartItem1.setCart(cart);
            cartItem1.setProducts(products);
            cartItem1.setQuantity(quantity);
            cartItem1.setUnitPrice(products.getPrice());
        }else {
            int newQuantity= cartItem1.getQuantity()+quantity;
            if ((newQuantity>products.getInventory())){
                throw  new RuntimeException("San pham"+products.getNameProduct()+"khong du. Hien con :"+products.getInventory());
            }cartItem1.setQuantity(newQuantity);
        }cartItem1.setTotalPrice();
        cart.addItem(cartItem1);
        cartItemRepository.save(cartItem1);
        cartRepository.save(cart);

    }
    @Transactional
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart=cartService.getCart(cartId);
        CartItem itemToRemove=getCartItem(cartId,productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }
    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart=cartService.getCart(cartId);
        cart.getCartItems()
                .stream().filter(item->item.getProducts().getId().equals(productId))
                .findFirst().ifPresent(item->{
            item.setQuantity(quantity);
            item.setUnitPrice(item.getProducts().getPrice());
            item.setTotalPrice();
        });cart.updateItemsTotalAmount();

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart=cartService.getCart(cartId);
        return cart.getCartItems()
                .stream().filter(item->item.getProducts().getId().equals(productId)).findFirst()
                .orElseThrow(()->new ResoureeNotFoundException("Item not found"));
    }
}
