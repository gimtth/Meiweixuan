package com.meiweixuan.service.impl;

import com.meiweixuan.dao.OrderDao;
import com.meiweixuan.entity.Order;
import com.meiweixuan.entity.OrderItem;
import com.meiweixuan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderDao.findAll();
        System.out.println("查询到订单数量: " + orders.size());
        if (orders.isEmpty()) {
            System.out.println("警告: 没有查询到任何订单数据!");
        } else {
            for (Order order : orders) {
                System.out.println("订单ID: " + order.getId() + ", 状态: " + order.getStatus());
            }
        }
        return orders;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderDao.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderDao.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        // 生成订单号
        String orderNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        order.setOrderNumber(orderNumber);
        order.setCreateTime(new Date());
        order.setStatus("待支付");
        order.setPaymentStatus("未支付");
        
                // 自动计算总价（商品总价+配送费）
        BigDecimal total = BigDecimal.ZERO;
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                if (item.getPrice() != null && item.getQuantity() != null) {
                    total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }
            }
        }
        if (order.getDeliveryFee() != null) {
            total = total.add(order.getDeliveryFee());
        }
        order.setAmount(total);
        
        return orderDao.save(order);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        order.setUpdateTime(new Date());
        
        // 根据状态更新其他时间字段
        if ("已配送".equals(status)) {
            order.setDeliveryTime(new Date());
        } else if ("已完成".equals(status)) {
            order.setCompleteTime(new Date());
        }
        
        return orderDao.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus("已取消");
        order.setUpdateTime(new Date());
        return orderDao.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderDao.delete(order);
    }
} 