package com.meiweixuan.service.impl;

import com.meiweixuan.dao.CartDao;
import com.meiweixuan.dao.DishDao;
import com.meiweixuan.dao.OrderDao;
import com.meiweixuan.dao.OrderItemDao;
import com.meiweixuan.dao.UserDao;
import com.meiweixuan.dto.OrderCreateRequest;
import com.meiweixuan.entity.Cart;
import com.meiweixuan.entity.Dish;
import com.meiweixuan.entity.Order;
import com.meiweixuan.entity.OrderItem;
import com.meiweixuan.entity.User;
import com.meiweixuan.exception.BusinessException;
import com.meiweixuan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private DishDao dishDao;

    @Autowired
    private UserDao userDao;

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
    public Order createOrder(Integer userId, OrderCreateRequest orderCreateRequest) {
        List<Cart> cartItems = cartDao.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new BusinessException(400, "购物车为空，无法提交订单");
        }

        User user = userDao.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        user.setName(orderCreateRequest.getName());
        user.setPhone(orderCreateRequest.getPhone());
        user.setAddress(orderCreateRequest.getAddress());
        userDao.save(user);

        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(orderCreateRequest.getAddress());
        order.setPhone(orderCreateRequest.getPhone());
        order.setNote(orderCreateRequest.getNote());
        order.setPaymentMethod(orderCreateRequest.getPaymentMethod());
        order.setDeliveryFee(new BigDecimal("5.00"));

        String orderNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        order.setOrderNumber(orderNumber);
        order.setCreateTime(new Date());

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart cartItem : cartItems) {
            Dish dish = dishDao.findById(cartItem.getDishId())
                    .orElseThrow(() -> new BusinessException(404, "菜品不存在"));

            if (dish.getStatus() == null || dish.getStatus() != 1) {
                throw new BusinessException(400, "菜品已下架，无法提交订单");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setDishId(dish.getId());
            orderItem.setName(dish.getName());
            orderItem.setPrice(dish.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setAmount(dish.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItem.setImage(dish.getImage());
            orderItems.add(orderItem);

            total = total.add(orderItem.getAmount());
            dish.setSales((dish.getSales() == null ? 0 : dish.getSales()) + cartItem.getQuantity());
            dishDao.save(dish);
        }

        if (order.getDeliveryFee() != null) {
            total = total.add(order.getDeliveryFee());
        }
        order.setAmount(total);

        if ("online".equalsIgnoreCase(orderCreateRequest.getPaymentMethod())) {
            order.setStatus("已支付待配送");
            order.setPaymentStatus("已支付");
            order.setPaymentTime(new Date());
        } else {
            order.setStatus("待支付");
            order.setPaymentStatus("未支付");
        }

        Order savedOrder = orderDao.save(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(savedOrder.getId());
        }
        orderItemDao.saveAll(orderItems);
        cartDao.deleteByUserId(userId);

        savedOrder.setOrderItems(orderItems);
        savedOrder.setUser(user);
        return savedOrder;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        order.setUpdateTime(new Date());
        
        // 根据状态更新其他时间字段
        if ("配送中".equals(status) || "已配送".equals(status)) {
            order.setDeliveryTime(new Date());
        } else if ("已完成".equals(status)) {
            order.setCompleteTime(new Date());
        }
        
        return orderDao.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id, Integer userId) {
        Order order = getOrderById(id);

        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权取消该订单");
        }
        if ("已完成".equals(order.getStatus()) || "已取消".equals(order.getStatus())) {
            throw new BusinessException(400, "当前订单状态不可取消");
        }

        order.setStatus("已取消");
        order.setUpdateTime(new Date());
        return orderDao.save(order);
    }

    @Override
    @Transactional
    public Order confirmOrderReceived(Long id, Integer userId) {
        Order order = getOrderById(id);

        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权确认该订单");
        }
        if (!"配送中".equals(order.getStatus()) && !"已配送".equals(order.getStatus())) {
            throw new BusinessException(400, "当前订单状态不可确认收货");
        }

        order.setStatus("已完成");
        order.setCompleteTime(new Date());
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
