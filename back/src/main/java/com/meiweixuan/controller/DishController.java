package com.meiweixuan.controller;

import com.meiweixuan.common.Result;
import com.meiweixuan.entity.Banner;
import com.meiweixuan.entity.Dish;
import com.meiweixuan.entity.Category;
import com.meiweixuan.service.DishService;
import com.meiweixuan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/caipin")
    public Result<List<Dish>> getAllDishes() {
        List<Dish> dishes = dishService.getAllDishes();
        return Result.success(dishes);
    }

    @GetMapping("/caipinfenlei")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }
    
    @GetMapping("/banner")
    public Result<List<Banner>> getBanners() {
        // 返回一些模拟的轮播图数据
        List<Banner> banners = new ArrayList<>();
        
        Banner banner1 = new Banner();
        banner1.setId(1);
        banner1.setTitle("美味轩特色菜");
        banner1.setDescription("尝尝我们的招牌菜");
        banner1.setImage("/images/dishes/banner1.jpg");
        banner1.setSort(1);
        banner1.setCreateTime(new Date());
        banner1.setStatus(1);
        banners.add(banner1);
        
        Banner banner2 = new Banner();
        banner2.setId(2);
        banner2.setTitle("新品上市");
        banner2.setDescription("全新菜品，限时优惠");
        banner2.setImage("/images/dishes/banner2.jpg");
        banner2.setSort(2);
        banner2.setCreateTime(new Date());
        banner2.setStatus(1);
        banners.add(banner2);
        
        Banner banner3 = new Banner();
        banner3.setId(3);
        banner3.setTitle("优惠活动");
        banner3.setDescription("下单满100减20");
        banner3.setImage("/images/dishes/banner3.jpg");
        banner3.setSort(3);
        banner3.setCreateTime(new Date());
        banner3.setStatus(1);
        banners.add(banner3);
        
        return Result.success(banners);
    }
} 