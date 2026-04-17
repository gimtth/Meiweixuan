package com.meiweixuan.service.impl;

import com.meiweixuan.dao.UserDao;
import com.meiweixuan.dto.LoginRequest;
import com.meiweixuan.dto.LoginResponse;
import com.meiweixuan.entity.User;
import com.meiweixuan.service.UserService;
import com.meiweixuan.util.JwtTokenUtil;
import com.meiweixuan.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordUtil passwordUtil;
    
    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userDao.findByUsername(loginRequest.getUsername());
        
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOptional.get();
        
        if (!passwordUtil.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        if (passwordUtil.needsUpgrade(user.getPassword())) {
            user.setPassword(passwordUtil.encode(loginRequest.getPassword()));
        }
        
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }
        
        // 更新最后登录时间
        userDao.updateLastLoginTime(user.getId(), new Date());
        
        // 生成token
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getId(), "user");
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setName(user.getName());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setAddress(user.getAddress());
        loginResponse.setRole(user.getRole());
        loginResponse.setToken(token);
        loginResponse.setUserType("user");
        
        return loginResponse;
    }
    
    @Override
    @Transactional
    public User register(User user) {
        // 检查用户名是否已存在
        if (userDao.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        user.setPassword(passwordUtil.encode(user.getPassword()));
        user.setRegisterTime(new Date());
        user.setStatus(1);
        user.setRole("ROLE_USER");
        
        return userDao.save(user);
    }
    
    @Override
    public User getUserById(Integer id) {
        return userDao.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
    
    @Override
    @Transactional
    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());
        
        existingUser.setName(user.getName());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        
        return userDao.save(existingUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        User user = getUserById(id);
        userDao.delete(user);
    }

    @Override
    @Transactional
    public User updateUserRole(Integer id, String role) {
        User user = getUserById(id);
        user.setRole(role);
        return userDao.save(user);
    }
} 
