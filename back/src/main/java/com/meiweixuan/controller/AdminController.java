package com.meiweixuan.controller;

import com.meiweixuan.common.Result;
import com.meiweixuan.dao.OrderItemDao;
import com.meiweixuan.dto.LoginRequest;
import com.meiweixuan.dto.LoginResponse;
import com.meiweixuan.entity.Admin;
import com.meiweixuan.entity.Category;
import com.meiweixuan.entity.Dish;
import com.meiweixuan.entity.Order;
import com.meiweixuan.entity.OrderItem;
import com.meiweixuan.entity.User;
import com.meiweixuan.service.AdminService;
import com.meiweixuan.service.CategoryService;
import com.meiweixuan.service.DishService;
import com.meiweixuan.service.OrderService;
import com.meiweixuan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private DishService dishService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderItemDao orderItemDao;
    
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = adminService.login(loginRequest);
        return Result.success(loginResponse);
    }
    
    @GetMapping("/info")
    public Result<Admin> getAdminInfo(HttpServletRequest request) {
        Integer adminId = (Integer) request.getAttribute("userId");
        String userType = (String) request.getAttribute("userType");
        
        if (!"admin".equals(userType)) {
            return Result.error("无权限访问");
        }
        
        Admin admin = adminService.getAdminById(adminId);
        return Result.success(admin);
    }
    
    @PutMapping("/update")
    public Result<Admin> updateAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        Integer adminId = (Integer) request.getAttribute("userId");
        String userType = (String) request.getAttribute("userType");
        
        if (!"admin".equals(userType)) {
            return Result.error("无权限访问");
        }
        
        admin.setId(adminId);
        Admin updatedAdmin = adminService.updateAdmin(admin);
        return Result.success(updatedAdmin);
    }
    
    // 分类管理
    @GetMapping("/categories")
    public Result<List<Category>> getAllCategories(HttpServletRequest request) {
        checkAdminPermission(request);
        List<Category> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }
    
    @GetMapping("/categories/{id}")
    public Result<Category> getCategoryById(@PathVariable Integer id, HttpServletRequest request) {
        checkAdminPermission(request);
        Category category = categoryService.getCategoryById(id);
        return Result.success(category);
    }
    
    @PostMapping("/categories")
    public Result<Category> createCategory(@RequestBody @Valid Category category, HttpServletRequest request) {
        checkAdminPermission(request);
        Category createdCategory = categoryService.saveCategory(category);
        return Result.success(createdCategory);
    }
    
    @PutMapping("/categories/{id}")
    public Result<Category> updateCategory(@PathVariable Integer id, @RequestBody @Valid Category category, HttpServletRequest request) {
        checkAdminPermission(request);
        category.setId(id);
        Category updatedCategory = categoryService.updateCategory(category);
        return Result.success(updatedCategory);
    }
    
    @DeleteMapping("/categories/{id}")
    public Result<String> deleteCategory(@PathVariable Integer id, HttpServletRequest request) {
        checkAdminPermission(request);
        categoryService.deleteCategory(id);
        return Result.success("分类删除成功");
    }
    
    // 菜品管理
    @GetMapping("/dishes")
    public Result<List<Dish>> getAllDishes(HttpServletRequest request) {
        checkAdminPermission(request);
        List<Dish> dishes = dishService.getAllDishes();
        return Result.success(dishes);
    }
    
    @GetMapping("/dishes/{id}")
    public Result<Dish> getDishById(@PathVariable Integer id, HttpServletRequest request) {
        checkAdminPermission(request);
        Dish dish = dishService.getDishById(id);
        return Result.success(dish);
    }
    
    @PostMapping("/dishes")
    public Result<Dish> createDish(@RequestBody @Valid Dish dish, HttpServletRequest request) {
        checkAdminPermission(request);
        Dish createdDish = dishService.saveDish(dish);
        return Result.success(createdDish);
    }
    
    @PutMapping("/dishes/{id}")
    public Result<Dish> updateDish(@PathVariable Integer id, @RequestBody @Valid Dish dish, HttpServletRequest request) {
        checkAdminPermission(request);
        dish.setId(id);
        Dish updatedDish = dishService.updateDish(dish);
        return Result.success(updatedDish);
    }
    
    @DeleteMapping("/dishes/{id}")
    public Result<String> deleteDish(@PathVariable Integer id, HttpServletRequest request) {
        checkAdminPermission(request);
        dishService.deleteDish(id);
        return Result.success("菜品删除成功");
    }
    
    // 订单管理
    @GetMapping("/orders")
    public Result<List<Order>> getAllOrders(HttpServletRequest request) {
        checkAdminPermission(request);
        System.out.println("管理员请求获取所有订单");
        List<Order> orders = orderService.getAllOrders();
        
        // 为每个订单加载订单项和用户信息
        for (Order order : orders) {
            try {
                // 加载订单项 - 使用新方法
                List<OrderItem> items = orderItemDao.findItemsByOrderId(order.getId());
                if (items.isEmpty()) {
                    System.out.println("警告: 订单ID " + order.getId() + " 没有找到订单项，尝试从dingdanxiangqing表查询");
                    // 可以在这里添加备用查询逻辑
                }
                order.setOrderItems(items);
                
                // 加载用户信息
                User user = userService.getUserById(order.getUserId());
                order.setUser(user);
                
                System.out.println("订单ID: " + order.getId() + ", 状态: " + order.getStatus() + 
                    ", 订单项数量: " + (items != null ? items.size() : 0) + 
                    ", 用户: " + (user != null ? user.getUsername() : "未知"));
            } catch (Exception e) {
                System.err.println("处理订单ID: " + order.getId() + " 时出错: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("返回订单数量: " + orders.size());
        return Result.success(orders);
    }
    
    @GetMapping("/orders/{id}")
    public Result<Order> getOrderById(@PathVariable Long id, HttpServletRequest request) {
        checkAdminPermission(request);
        Order order = orderService.getOrderById(id);
        
        // 加载订单项 - 使用新方法
        List<OrderItem> items = orderItemDao.findItemsByOrderId(order.getId());
        if (items.isEmpty()) {
            System.out.println("警告: 订单ID " + order.getId() + " 没有找到订单项，尝试从dingdanxiangqing表查询");
            // 可以在这里添加备用查询逻辑
        }
        order.setOrderItems(items);
        
        // 加载用户信息
        User user = userService.getUserById(order.getUserId());
        order.setUser(user);
        
        return Result.success(order);
    }
    
    @PutMapping("/orders/{id}/status")
    public Result<Order> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap, HttpServletRequest request) {
        checkAdminPermission(request);
        String status = statusMap.get("status");
        if (status == null || status.isEmpty()) {
            return Result.error("状态不能为空");
        }
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return Result.success(updatedOrder);
    }
    
    // 用户管理
    @GetMapping("/users")
    public Result<List<User>> getAllUsers(HttpServletRequest request) {
        checkAdminPermission(request);
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }
    
    @GetMapping("/users/{id}")
    public Result<User> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        checkAdminPermission(request);
        User user = userService.getUserById(id);
        return Result.success(user);
    }
    
    @PutMapping("/users/{id}")
    public Result<User> updateUser(@PathVariable Integer id, @RequestBody @Valid User user, HttpServletRequest request) {
        checkAdminPermission(request);
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return Result.success(updatedUser);
    }
    
    @DeleteMapping("/users/{id}")
    public Result<String> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        checkAdminPermission(request);
        userService.deleteUser(id);
        return Result.success("用户删除成功");
    }
    
    @PutMapping("/users/{id}/role")
    public Result<User> updateUserRole(@PathVariable Integer id, @RequestBody Map<String, String> roleMap, HttpServletRequest request) {
        checkAdminPermission(request);
        String role = roleMap.get("role");
        if (role == null || role.isEmpty()) {
            return Result.error("角色不能为空");
        }
        User updatedUser = userService.updateUserRole(id, role);
        return Result.success(updatedUser);
    }
    
    /**
     * 数据统计总览接口
     */
    @GetMapping("/stats/overview")
    public Result<Map<String, Object>> getStatisticsOverview() {
        Map<String, Object> data = new HashMap<>();
        // 订单总数
        int totalOrders = orderService.getAllOrders().size();
        // 总销售额
        BigDecimal totalSales = orderService.getAllOrders().stream()
                .map(o -> o.getAmount() == null ? BigDecimal.ZERO : o.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 用户总数
        int totalUsers = userService.getAllUsers().size();
        // 菜品总数
        int totalDishes = dishService.getAllDishes().size();
        // 分类总数
        int categoryCount = categoryService.getAllCategories().size();

        // 近7天订单量和销售额
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Map<String, Object>> recentOrders = new java.util.ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(df);
            long count = orderService.getAllOrders().stream()
                    .filter(o -> o.getCreateTime() != null && df.format(o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()).equals(dateStr))
                    .count();
            BigDecimal amount = orderService.getAllOrders().stream()
                    .filter(o -> o.getCreateTime() != null && df.format(o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()).equals(dateStr))
                    .map(o -> o.getAmount() == null ? BigDecimal.ZERO : o.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, Object> day = new HashMap<>();
            day.put("date", dateStr);
            day.put("count", count);
            day.put("amount", amount);
            recentOrders.add(day);
        }

        // 热门菜品（销量前5）
        List<Dish> allDishes = dishService.getAllDishes();
        List<Map<String, Object>> topDishes = new java.util.ArrayList<>();
        allDishes.stream()
                .sorted((d1, d2) -> Integer.compare(d2.getSales(), d1.getSales()))
                .limit(5)
                .forEach(dish -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", dish.getId());
                    map.put("name", dish.getName());
                    map.put("count", dish.getSales());
                    map.put("amount", dish.getPrice().multiply(BigDecimal.valueOf(dish.getSales())));
                    map.put("percent", 0); // 可后续补充
                    topDishes.add(map);
                });

        // 分类销售占比（按菜品销量）
        List<Category> categories = categoryService.getAllCategories();
        List<Map<String, Object>> categoryStats = new java.util.ArrayList<>();
        int totalSalesCount = allDishes.stream().mapToInt(Dish::getSales).sum();
        for (Category cat : categories) {
            int catSales = allDishes.stream().filter(d -> d.getCategoryId().equals(cat.getId())).mapToInt(Dish::getSales).sum();
            Map<String, Object> map = new HashMap<>();
            map.put("name", cat.getName());
            map.put("value", catSales);
            categoryStats.add(map);
        }

        // 增长率（示例，实际可根据业务调整）
        data.put("orderTrend", 0);
        data.put("saleTrend", 0);
        data.put("userTrend", 0);

        data.put("totalOrders", totalOrders);
        data.put("totalSales", totalSales);
        data.put("totalUsers", totalUsers);
        data.put("totalDishes", totalDishes);
        data.put("categoryCount", categoryCount);
        data.put("recentOrders", recentOrders);
        data.put("topDishes", topDishes);
        data.put("categoryStats", categoryStats);
        return Result.success(data);
    }
    
    /**
     * 检查管理员权限
     * @param request HTTP请求
     */
    private void checkAdminPermission(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String userType = (String) request.getAttribute("userType");
        
        if (userId == null || !"admin".equals(userType)) {
            throw new RuntimeException("无管理员权限");
        }
    }
} 