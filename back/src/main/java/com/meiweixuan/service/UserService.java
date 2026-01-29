package com.meiweixuan.service;

import com.meiweixuan.dto.LoginRequest;
import com.meiweixuan.dto.LoginResponse;
import com.meiweixuan.entity.User;

import java.util.List;

public interface UserService {
    
    LoginResponse login(LoginRequest loginRequest);
    
    User register(User user);
    
    User getUserById(Integer id);
    
    User updateUser(User user);
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    List<User> getAllUsers();
    
    /**
     * 删除用户
     * @param id 用户ID
     */
    void deleteUser(Integer id);
    
    /**
     * 更新用户角色
     * @param id 用户ID
     * @param role 角色名称
     * @return 更新后的用户
     */
    User updateUserRole(Integer id, String role);
} 