package com.meiweixuan.controller;

import com.meiweixuan.common.Result;
import com.meiweixuan.dto.CartItemRequest;
import com.meiweixuan.dto.CartResponse;
import com.meiweixuan.entity.Cart;
import com.meiweixuan.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public Result<CartResponse> getCart(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        CartResponse cartResponse = cartService.getCartByUserId(userId);
        return Result.success(cartResponse);
    }
    
    @PostMapping("/add")
    public Result<CartResponse> addToCart(@RequestBody CartItemRequest cartItemRequest, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        CartResponse cartResponse = cartService.addToCart(userId, cartItemRequest);
        return Result.success(cartResponse);
    }
    
    @PutMapping("/update")
    public Result<CartResponse> updateCartItem(@RequestBody CartItemRequest cartItemRequest, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        CartResponse cartResponse = cartService.updateCartItem(userId, cartItemRequest);
        return Result.success(cartResponse);
    }
    
    @DeleteMapping("/item/{cartItemId}")
    public Result<CartResponse> removeCartItem(@PathVariable Integer cartItemId, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        CartResponse cartResponse = cartService.removeCartItem(userId, cartItemId);
        return Result.success(cartResponse);
    }
    
    @DeleteMapping("/clear")
    public Result<CartResponse> clearCart(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        CartResponse cartResponse = cartService.clearCart(userId);
        return Result.success(cartResponse);
    }
} 