package com.meiweixuan.controller;

import com.meiweixuan.common.Result;
import com.meiweixuan.dto.LoginRequest;
import com.meiweixuan.dto.LoginResponse;
import com.meiweixuan.entity.User;
import com.meiweixuan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/yonghu")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return Result.success(loginResponse);
    }
    
    @PostMapping("/register")
    public Result<User> register(@RequestBody @Valid User user) {
        User registeredUser = userService.register(user);
        return Result.success(registeredUser);
    }
    
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String userType = (String) request.getAttribute("userType");
        
        if (!"user".equals(userType)) {
            return Result.error("无权限访问");
        }
        
        User user = userService.getUserById(userId);
        return Result.success(user);
    }
    
    @PutMapping("/update")
    public Result<User> updateUser(@RequestBody User user, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String userType = (String) request.getAttribute("userType");
        
        if (!"user".equals(userType)) {
            return Result.error("无权限访问");
        }
        
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return Result.success(updatedUser);
    }
} 