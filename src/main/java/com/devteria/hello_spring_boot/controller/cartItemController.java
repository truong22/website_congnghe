package com.devteria.hello_spring_boot.controller;

import com.devteria.hello_spring_boot.Entity.Cart;
import com.devteria.hello_spring_boot.Entity.User;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Response.ApiResponse;
import com.devteria.hello_spring_boot.Service.Cart.ICartService;
import com.devteria.hello_spring_boot.Service.CartItem.ICartItemService;
import com.devteria.hello_spring_boot.Service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems/")
public class cartItemController {
    private final ICartService cartService;
    private final ICartItemService cartItemService;
    private final IUserService userService;
    @PostMapping("item/add")
    public ResponseEntity<ApiResponse>addItemToCart(
            @RequestParam Long productId,@RequestParam Integer quantity){
        try {
            User user=userService.getAuthenticatedUser();
             Cart cart=cartService.initializeNewCart(user);

            cartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success",null));
        }catch (ResoureeNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("remove/cart/{cartId}/item/{productId}")
    public ResponseEntity<ApiResponse>deleteItemFormCart(@PathVariable Long cartId, @PathVariable Long productId){
        try {
            cartItemService.removeItemFromCart(cartId,productId);
            return ResponseEntity.ok(new ApiResponse("Add Item Success",null));
        }catch (ResoureeNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }

    @PutMapping("cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse>updateItemFormCart(@PathVariable Long cartId
            , @PathVariable Long itemId,@RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId,itemId,quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success",null));
        }catch (ResoureeNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }
}
