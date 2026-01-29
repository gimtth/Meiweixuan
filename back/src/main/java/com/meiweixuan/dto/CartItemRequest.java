package com.meiweixuan.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemRequest {
    
    private Integer cartItemId;
    
    @NotNull(message = "菜品ID不能为空")
    private Integer dishId;
    
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量最小为1")
    private Integer quantity;
    
    public Integer getCartItemId() {
        return cartItemId;
    }
    
    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }
    
    public Integer getDishId() {
        return dishId;
    }
    
    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
} 