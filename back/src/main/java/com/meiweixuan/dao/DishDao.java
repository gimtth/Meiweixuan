package com.meiweixuan.dao;

import com.meiweixuan.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishDao extends JpaRepository<Dish, Integer> {
    
    List<Dish> findByCategoryId(Integer categoryId);
    
    List<Dish> findByIsFeatured(Integer isFeatured);
    
    @Query("select d from Dish d where d.status = 1 order by d.sales desc")
    List<Dish> findTopSales();
} 