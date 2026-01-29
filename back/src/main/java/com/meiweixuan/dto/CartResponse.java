package com.meiweixuan.dto;

import com.meiweixuan.entity.Cart;
import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    
    private List<CartDTO> items;
    private BigDecimal totalAmount;
    private Integer totalItems;
    
    public static class CartDTO {
        private Integer cartItemId;
        private Integer dishId;
        private Integer quantity;
        private DishDTO dish;
        
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
        
        public DishDTO getDish() {
            return dish;
        }
        
        public void setDish(DishDTO dish) {
            this.dish = dish;
        }
    }
    
    public static class DishDTO {
        private Integer id;
        private String name;
        private BigDecimal price;
        private String image;
        
        public Integer getId() {
            return id;
        }
        
        public void setId(Integer id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public BigDecimal getPrice() {
            return price;
        }
        
        public void setPrice(BigDecimal price) {
            this.price = price;
        }
        
        public String getImage() {
            return image;
        }
        
        public void setImage(String image) {
            this.image = image;
        }
    }
    
    public List<CartDTO> getItems() {
        return items;
    }
    
    public void setItems(List<CartDTO> items) {
        this.items = items;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public Integer getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
} 