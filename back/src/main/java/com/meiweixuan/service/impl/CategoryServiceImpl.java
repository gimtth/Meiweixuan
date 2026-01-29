package com.meiweixuan.service.impl;

import com.meiweixuan.dao.CategoryDao;
import com.meiweixuan.entity.Category;
import com.meiweixuan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        category.setCreateTime(new Date());
        category.setStatus(1);
        return categoryDao.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        Category existingCategory = getCategoryById(category.getId());
        
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setSort(category.getSort());
        existingCategory.setUpdateTime(new Date());
        
        return categoryDao.save(existingCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        Category category = getCategoryById(id);
        categoryDao.delete(category);
    }
} 