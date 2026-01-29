package com.meiweixuan.service.impl;

import com.meiweixuan.dao.DishDao;
import com.meiweixuan.entity.Dish;
import com.meiweixuan.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishDao dishDao;

    @Override
    public List<Dish> getAllDishes() {
        return dishDao.findAll();
    }

    @Override
    public Dish getDishById(Integer id) {
        return dishDao.findById(id)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
    }

    @Override
    public List<Dish> getDishesByCategory(Integer categoryId) {
        return dishDao.findByCategoryId(categoryId);
    }

    @Override
    @Transactional
    public Dish saveDish(Dish dish) {
        dish.setCreateTime(new Date());
        dish.setStatus(1);
        if (dish.getSales() == null) {
            dish.setSales(0);
        }
        return dishDao.save(dish);
    }

    @Override
    @Transactional
    public Dish updateDish(Dish dish) {
        Dish existingDish = getDishById(dish.getId());
        
        existingDish.setName(dish.getName());
        existingDish.setCategoryId(dish.getCategoryId());
        existingDish.setPrice(dish.getPrice());
        existingDish.setDescription(dish.getDescription());
        existingDish.setImage(dish.getImage());
        existingDish.setIsFeatured(dish.getIsFeatured());
        existingDish.setUpdateTime(new Date());
        
        return dishDao.save(existingDish);
    }

    @Override
    @Transactional
    public void deleteDish(Integer id) {
        Dish dish = getDishById(id);
        dishDao.delete(dish);
    }

    @Override
    public List<Dish> getFeaturedDishes() {
        return dishDao.findByIsFeatured(1);
    }

    @Override
    public List<Dish> getTopSalesDishes() {
        return dishDao.findTopSales();
    }
} 