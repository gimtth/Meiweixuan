package com.meiweixuan.dao;

import com.meiweixuan.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartDao extends JpaRepository<Cart, Integer> {
    
    /**
     * 根据用户ID查询购物车
     * @param userId 用户ID
     * @return 购物车项列表
     */
    List<Cart> findByUserId(Integer userId);
    
    /**
     * 根据用户ID和菜品ID查询购物车项
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 购物车项
     */
    Optional<Cart> findByUserIdAndDishId(Integer userId, Integer dishId);
    
    /**
     * 根据用户ID删除所有购物车项
     * @param userId 用户ID
     */
    @Modifying
    @Query("delete from Cart c where c.userId = :userId")
    void deleteByUserId(@Param("userId") Integer userId);
} 