package com.meiweixuan.service;

import com.meiweixuan.entity.Dish;

import java.util.List;

public interface DishService {
    
    /**
     * 获取所有菜品
     * @return 菜品列表
     */
    List<Dish> getAllDishes();
    
    /**
     * 根据ID获取菜品
     * @param id 菜品ID
     * @return 菜品对象
     */
    Dish getDishById(Integer id);
    
    /**
     * 根据分类ID获取菜品
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    List<Dish> getDishesByCategory(Integer categoryId);
    
    /**
     * 保存菜品
     * @param dish 菜品对象
     * @return 保存后的菜品对象
     */
    Dish saveDish(Dish dish);
    
    /**
     * 更新菜品
     * @param dish 菜品对象
     * @return 更新后的菜品对象
     */
    Dish updateDish(Dish dish);
    
    /**
     * 删除菜品
     * @param id 菜品ID
     */
    void deleteDish(Integer id);
    
    /**
     * 获取推荐菜品
     * @return 推荐菜品列表
     */
    List<Dish> getFeaturedDishes();
    
    /**
     * 获取销量最高的菜品
     * @return 销量最高的菜品列表
     */
    List<Dish> getTopSalesDishes();
} 