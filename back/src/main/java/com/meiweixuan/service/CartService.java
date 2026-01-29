package com.meiweixuan.service;

import com.meiweixuan.dto.CartItemRequest;
import com.meiweixuan.dto.CartResponse;
import com.meiweixuan.entity.Cart;

import java.util.List;

public interface CartService {
    
    /**
     * 获取用户购物车
     * @param userId 用户ID
     * @return 购物车响应对象
     */
    CartResponse getCartByUserId(Integer userId);
    
    /**
     * 添加菜品到购物车
     * @param userId 用户ID
     * @param cartItemRequest 购物车项请求
     * @return 更新后的购物车
     */
    CartResponse addToCart(Integer userId, CartItemRequest cartItemRequest);
    
    /**
     * 更新购物车项
     * @param userId 用户ID
     * @param cartItemRequest 购物车项请求
     * @return 更新后的购物车
     */
    CartResponse updateCartItem(Integer userId, CartItemRequest cartItemRequest);
    
    /**
     * 删除购物车项
     * @param userId 用户ID
     * @param cartItemId 购物车项ID
     * @return 更新后的购物车
     */
    CartResponse removeCartItem(Integer userId, Integer cartItemId);
    
    /**
     * 清空购物车
     * @param userId 用户ID
     * @return 空购物车
     */
    CartResponse clearCart(Integer userId);
} 