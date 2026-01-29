package com.meiweixuan.dao;

import com.meiweixuan.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {
    /**
     * 根据订单ID查询订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> findByOrderId(Long orderId);
    
    /**
     * 使用原生SQL查询订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    @Query(value = "SELECT * FROM order_items WHERE order_id = :orderId", nativeQuery = true)
    List<OrderItem> findItemsByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 检查订单项是否存在
     * @param orderId 订单ID
     * @return 订单项数量
     */
    int countByOrderId(Long orderId);
} 