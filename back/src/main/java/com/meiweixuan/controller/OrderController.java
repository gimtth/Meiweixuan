package com.meiweixuan.controller;

import com.meiweixuan.entity.Order;
import com.meiweixuan.entity.OrderItem;
import com.meiweixuan.entity.User;
import com.meiweixuan.service.OrderService;
import com.meiweixuan.service.UserService;
import com.meiweixuan.dao.OrderItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemDao orderItemDao;
    
    @Autowired
    private UserService userService;

    // 获取当前登录用户的订单列表
    @GetMapping("/orders")
    public List<Order> getUserOrders(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("未登录");
        }
        List<Order> orders = orderService.getOrdersByUserId(userId);
        for (Order order : orders) {
            List<OrderItem> items = orderItemDao.findByOrderId(order.getId());
            order.setOrderItems(items);
        }
        return orders;
    }

    // 获取订单详情
    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("未登录");
        }
        Order order = orderService.getOrderById(id);
        // 权限校验：只能查自己的订单
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看该订单");
        }
        // 查明细
        List<OrderItem> items = orderItemDao.findByOrderId(order.getId());
        order.setOrderItems(items);
        
        // 查询并设置用户信息
        User user = userService.getUserById(order.getUserId());
        order.setUser(user);
        
        return order;
    }
} 