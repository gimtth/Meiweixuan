package com.meiweixuan.service;

import com.meiweixuan.entity.Category;

import java.util.List;

public interface CategoryService {
    
    /**
     * 获取所有分类
     * @return 分类列表
     */
    List<Category> getAllCategories();
    
    /**
     * 根据ID获取分类
     * @param id 分类ID
     * @return 分类对象
     */
    Category getCategoryById(Integer id);
    
    /**
     * 保存分类
     * @param category 分类对象
     * @return 保存后的分类对象
     */
    Category saveCategory(Category category);
    
    /**
     * 更新分类
     * @param category 分类对象
     * @return 更新后的分类对象
     */
    Category updateCategory(Category category);
    
    /**
     * 删除分类
     * @param id 分类ID
     */
    void deleteCategory(Integer id);
} 