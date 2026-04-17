package com.meiweixuan.service;

import com.meiweixuan.dto.OrderCreateRequest;
import com.meiweixuan.entity.Order;

import java.util.List;

public interface OrderService {
    
    /**
     * 获取所有订单
     * @return 订单列表
     */
    List<Order> getAllOrders();
    
    /**
     * 根据ID获取订单
     * @param id 订单ID
     * @return 订单对象
     */
    Order getOrderById(Long id);
    
    /**
     * 根据用户ID获取订单
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> getOrdersByUserId(Integer userId);
    
    /**
     * 创建订单
     * @param order 订单对象
     * @return 创建后的订单对象
     */
    Order createOrder(Integer userId, OrderCreateRequest orderCreateRequest);
    
    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 订单状态
     * @return 更新后的订单对象
     */
    Order updateOrderStatus(Long id, String status);
    
    /**
     * 取消订单
     * @param id 订单ID
     * @return 取消后的订单对象
     */
    Order cancelOrder(Long id, Integer userId);

    /**
     * 用户确认收货
     * @param id 订单ID
     * @param userId 用户ID
     * @return 更新后的订单对象
     */
    Order confirmOrderReceived(Long id, Integer userId);
    
    /**
     * 删除订单
     * @param id 订单ID
     */
    void deleteOrder(Long id);
} 
