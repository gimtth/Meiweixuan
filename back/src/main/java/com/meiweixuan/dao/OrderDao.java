package com.meiweixuan.dao;

import com.meiweixuan.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
    
    List<Order> findByUserId(Integer userId);
} 