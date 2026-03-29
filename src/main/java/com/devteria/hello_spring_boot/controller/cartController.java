package com.devteria.hello_spring_boot.controller;


import com.devteria.hello_spring_boot.Entity.Cart;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Response.ApiResponse;
import com.devteria.hello_spring_boot.Service.Cart.ICartService;
import com.devteria.hello_spring_boot.Service.CartItem.ICartItemService;
import com.devteria.hello_spring_boot.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts/")
public class cartController {
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @GetMapping("{cartId}/my_cart")
    private ResponseEntity<ApiResponse> getCartById(@PathVariable Long cartId){
        try {
            CartDto cartDto = cartService.getCartDto(cartId);
            return ResponseEntity.ok(new ApiResponse("Ok",cartDto));
        }catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @DeleteMapping("delete/{cartId}/cart")
    private ResponseEntity<ApiResponse> deleteCart(@PathVariable Long cartId){
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Ok delete",null));
        }catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }



    @GetMapping("cart/total_amount/{cartId}")
    private ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){
        try {
           BigDecimal totalAmount =cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Ok",totalAmount));
        }catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }



}
