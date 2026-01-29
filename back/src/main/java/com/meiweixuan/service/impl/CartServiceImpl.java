package com.meiweixuan.service.impl;

import com.meiweixuan.dao.CartDao;
import com.meiweixuan.dao.DishDao;
import com.meiweixuan.dto.CartItemRequest;
import com.meiweixuan.dto.CartResponse;
import com.meiweixuan.entity.Cart;
import com.meiweixuan.entity.Dish;
import com.meiweixuan.exception.BusinessException;
import com.meiweixuan.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartDao cartDao;
    
    @Autowired
    private DishDao dishDao;
    
    @Override
    public CartResponse getCartByUserId(Integer userId) {
        List<Cart> cartItems = cartDao.findByUserId(userId);
        return buildCartResponse(cartItems);
    }
    
    @Override
    @Transactional
    public CartResponse addToCart(Integer userId, CartItemRequest cartItemRequest) {
        // 验证菜品是否存在
        Dish dish = dishDao.findById(cartItemRequest.getDishId())
                .orElseThrow(() -> new BusinessException("菜品不存在"));
        
        // 检查该用户的购物车中是否已有该菜品
        Optional<Cart> existingCartItem = cartDao.findByUserIdAndDishId(userId, cartItemRequest.getDishId());
        
        if (existingCartItem.isPresent()) {
            // 如果已存在，更新数量
            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
            cartItem.setUpdateTime(new Date());
            cartDao.save(cartItem);
        } else {
            // 如果不存在，创建新购物车项
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setDishId(cartItemRequest.getDishId());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setCreateTime(new Date());
            cartDao.save(cartItem);
        }
        
        // 获取更新后的购物车并返回
        List<Cart> cartItems = cartDao.findByUserId(userId);
        return buildCartResponse(cartItems);
    }
    
    @Override
    @Transactional
    public CartResponse updateCartItem(Integer userId, CartItemRequest cartItemRequest) {
        if (cartItemRequest.getCartItemId() == null) {
            throw new BusinessException("购物车项ID不能为空");
        }
        
        // 验证购物车项是否存在且属于该用户
        Cart cartItem = cartDao.findById(cartItemRequest.getCartItemId())
                .orElseThrow(() -> new BusinessException("购物车项不存在"));
        
        if (!cartItem.getUserId().equals(userId)) {
            throw new BusinessException("无权限操作该购物车项");
        }
        
        // 更新数量
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setUpdateTime(new Date());
        cartDao.save(cartItem);
        
        // 获取更新后的购物车并返回
        List<Cart> cartItems = cartDao.findByUserId(userId);
        return buildCartResponse(cartItems);
    }
    
    @Override
    @Transactional
    public CartResponse removeCartItem(Integer userId, Integer cartItemId) {
        // 验证购物车项是否存在且属于该用户
        Cart cartItem = cartDao.findById(cartItemId)
                .orElseThrow(() -> new BusinessException("购物车项不存在"));
        
        if (!cartItem.getUserId().equals(userId)) {
            throw new BusinessException("无权限操作该购物车项");
        }
        
        // 删除购物车项
        cartDao.deleteById(cartItemId);
        
        // 获取更新后的购物车并返回
        List<Cart> cartItems = cartDao.findByUserId(userId);
        return buildCartResponse(cartItems);
    }
    
    @Override
    @Transactional
    public CartResponse clearCart(Integer userId) {
        // 删除该用户的所有购物车项
        cartDao.deleteByUserId(userId);
        
        // 返回空购物车
        return buildCartResponse(new ArrayList<>());
    }
    
    /**
     * 构建购物车响应对象
     * @param cartItems 购物车项列表
     * @return 购物车响应对象
     */
    private CartResponse buildCartResponse(List<Cart> cartItems) {
        CartResponse cartResponse = new CartResponse();
        List<CartResponse.CartDTO> cartDTOList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalItems = 0;
        
        for (Cart cartItem : cartItems) {
            CartResponse.CartDTO cartDTO = new CartResponse.CartDTO();
            cartDTO.setCartItemId(cartItem.getId());
            cartDTO.setDishId(cartItem.getDishId());
            cartDTO.setQuantity(cartItem.getQuantity());
            
            // 获取菜品信息（强制查库，确保不为null）
            Dish dish = cartItem.getDish();
            if (dish == null) {
                // 主动查一次，确保dish不为null
                dish = dishDao.findById(cartItem.getDishId()).orElse(null);
            }
            if (dish != null) {
                CartResponse.DishDTO dishDTO = new CartResponse.DishDTO();
                dishDTO.setId(dish.getId());
                dishDTO.setName(dish.getName());
                dishDTO.setPrice(dish.getPrice());
                dishDTO.setImage(dish.getImage());
                cartDTO.setDish(dishDTO);
                // 计算小计金额并累加
                BigDecimal itemAmount = dish.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
                totalAmount = totalAmount.add(itemAmount);
                totalItems += cartItem.getQuantity();
            } else {
                cartDTO.setDish(null);
            }
            cartDTOList.add(cartDTO);
        }
        
        cartResponse.setItems(cartDTOList);
        cartResponse.setTotalAmount(totalAmount);
        cartResponse.setTotalItems(totalItems);
        
        return cartResponse;
    }
} 